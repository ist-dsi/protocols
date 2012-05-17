/**
 * 
 */
package module.protocols.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import module.protocols.domain.ProtocolAuthorizationGroup;
import myorg.domain.groups.PersistentGroup;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class AuthorizationGroupBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8479983429517700272L;

    private List<PersistentGroup> authorizedGroups;

    private final ProtocolAuthorizationGroup group;

    public AuthorizationGroupBean(ProtocolAuthorizationGroup group) {
	authorizedGroups = new ArrayList<PersistentGroup>(group.getAuthorizedReaderGroups());
	this.group = group;
    }

    public List<PersistentGroup> getAuthorizedGroups() {
	return authorizedGroups;
    }

    public void setAuthorizedGroups(List<PersistentGroup> authorizedGroups) {
	this.authorizedGroups = authorizedGroups;
    }

    public ProtocolAuthorizationGroup getGroup() {
	return group;
    }

}
