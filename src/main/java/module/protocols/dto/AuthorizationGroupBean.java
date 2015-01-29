/**
 * 
 */
package module.protocols.dto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import module.protocols.domain.ProtocolAuthorizationGroup;

import org.fenixedu.bennu.core.groups.Group;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class AuthorizationGroupBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8479983429517700272L;

    private List<Group> authorizedGroups;

    private final ProtocolAuthorizationGroup group;

    public AuthorizationGroupBean(ProtocolAuthorizationGroup group) {
        authorizedGroups = group.getAuthorizedReaders().collect(Collectors.toList());
        this.group = group;
    }

    public List<Group> getAuthorizedGroups() {
        return authorizedGroups;
    }

    public void setAuthorizedGroups(List<Group> authorizedGroups) {
        this.authorizedGroups = authorizedGroups;
    }

    public ProtocolAuthorizationGroup getGroup() {
        return group;
    }

}
