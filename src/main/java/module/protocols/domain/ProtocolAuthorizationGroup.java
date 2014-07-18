package module.protocols.domain;

import java.util.Objects;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.Atomic;

public class ProtocolAuthorizationGroup extends ProtocolAuthorizationGroup_Base {

    private ProtocolAuthorizationGroup(Group writerGroup) {
        super();
        Objects.requireNonNull(writerGroup);
        this.setAuthorizedWriterGroup(writerGroup.toPersistentGroup());
        this.setProtocolManager(ProtocolManager.getInstance());
    }

    @Atomic
    public static Boolean createGroupWithWriter(Group group) {
        PersistentGroup persistent = group.toPersistentGroup();
        if (persistent.getProtocolAuthorizationGroupAllowedToWrite() != null) {
            return false;
        }
        new ProtocolAuthorizationGroup(group);
        return true;
    }

    @Atomic
    public void delete() {
        if (!getWriterProtocolsSet().isEmpty()) {
            throw ProtocolsDomainException.groupHasProtocols();
        }

        for (PersistentGroup group : getAuthorizedReaderGroupsSet()) {
            removeAuthorizedReaderGroups(group);
        }
        this.setAuthorizedWriterGroup(null);

        this.setProtocolManager(null);
        this.deleteDomainObject();
    }

    public Stream<Group> getAuthorizedReaders() {
        return getAuthorizedReaderGroupsSet().stream().map(PersistentGroup::toGroup);
    }

    public boolean isAccessible() {
        return getAuthorizedWriterGroup().isMember(Authenticate.getUser());
    }

    public Group getWriterGroup() {
        return getAuthorizedWriterGroup().toGroup();
    }

    @Atomic
    public void grant(Group readerGroup) {
        addAuthorizedReaderGroups(readerGroup.toPersistentGroup());
    }

    @Atomic
    public void revoke(PersistentGroup group) {
        removeAuthorizedReaderGroups(group);
    }

}
