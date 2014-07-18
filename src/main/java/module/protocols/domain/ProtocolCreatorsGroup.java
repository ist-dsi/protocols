package module.protocols.domain;

import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.CustomGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.joda.time.DateTime;

@GroupOperator("protocolCreators")
public class ProtocolCreatorsGroup extends CustomGroup {

    private static final long serialVersionUID = -8554541083518764360L;

    private static final Group INSTANCE = new ProtocolCreatorsGroup();

    @Override
    public boolean equals(Object other) {
        return other != null && other.getClass().equals(ProtocolCreatorsGroup.class);
    }

    @Override
    public Set<User> getMembers() {
        return ProtocolManager.getInstance().getProtocolAuthorizationGroupsSet().stream()
                .flatMap((group) -> group.getAuthorizedWriterGroup().getMembers().stream()).collect(Collectors.toSet());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public String getPresentationName() {
        return "Protocol Creators";
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean isMember(User user) {
        return ProtocolManager.getInstance().getProtocolAuthorizationGroupsSet().stream()
                .filter(ProtocolAuthorizationGroup::isAccessible).findAny().isPresent();
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return ProtocolManager.getInstance().getCreatorsGroup();
    }

    public static Group get() {
        return INSTANCE;
    }

}
