package module.protocols.domain;

import java.util.List;

import module.fileManagement.domain.ContextPath;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.domain.groups.PersistentGroup;
import pt.ist.fenixframework.Atomic;

public class ProtocolAuthorizationGroup extends ProtocolAuthorizationGroup_Base {

    public ProtocolAuthorizationGroup(PersistentGroup writerGroup) {
        super();
        this.setAuthorizedWriterGroup(writerGroup);
        this.setProtocolManager(ProtocolManager.getInstance());
        ProtocolManager.getInstance().getCreatorsGroup().addPersistentGroups(writerGroup);
        this.setGroupDir(new ProtocolDirNode(this));
    }

    @Atomic
    public static Boolean createGroupWithWriter(PersistentGroup group) {
        if (containsGroup(group)) {
            return false;
        } else {
            new ProtocolAuthorizationGroup(group);
            return true;
        }
    }

    public static boolean containsGroup(PersistentGroup group) {
        for (ProtocolAuthorizationGroup authGroup : ProtocolManager.getInstance().getProtocolAuthorizationGroups()) {
            if (authGroup.getAuthorizedWriterGroup().equals(group)) {
                return true;
            }
        }
        return false;
    }

    @Atomic
    public void delete() {

        if (!getWriterProtocols().isEmpty()) {
            throw new DomainException("error.group.has.protocols");
        }

        ProtocolManager.getInstance().getCreatorsGroup().removePersistentGroups(getAuthorizedWriterGroup());

        ProtocolDirNode groupDir = this.getGroupDir();

        groupDir.trash(new ContextPath(groupDir));

        this.setGroupDir(null);
        this.removeReaders();
        this.setProtocolManager(null);
        this.setAuthorizedWriterGroup(null);

        this.deleteDomainObject();
    }

    @Atomic
    public void updateReaders(List<PersistentGroup> authorizedGroups) {
        for (PersistentGroup existing : getAuthorizedReaderGroups()) {
            if (!authorizedGroups.contains(existing)) {
                removeAuthorizedReaderGroups(existing);
            }
        }

        for (PersistentGroup newGroup : authorizedGroups) {
            addAuthorizedReaderGroups(newGroup);
        }
    }

    private void removeReaders() {
        for (PersistentGroup group : getAuthorizedReaderGroups()) {
            removeAuthorizedReaderGroups(group);
        }
    }

    @Deprecated
    public java.util.Set<pt.ist.bennu.core.domain.groups.PersistentGroup> getAuthorizedReaderGroups() {
        return getAuthorizedReaderGroupsSet();
    }

    @Deprecated
    public java.util.Set<module.protocols.domain.Protocol> getWriterProtocols() {
        return getWriterProtocolsSet();
    }

}
