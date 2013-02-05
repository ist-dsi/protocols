/**
 * 
 */
package module.protocols.dto;

import java.io.Serializable;

import module.organization.domain.OrganizationalModel;
import module.protocols.domain.ProtocolManager;
import pt.ist.bennu.core.domain.groups.PersistentGroup;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class ProtocolSystemConfigurationBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2000558610665541013L;

    private OrganizationalModel internalOrganizationalModel;

    private OrganizationalModel externalOrganizationalModel;

    private PersistentGroup administrativeGroup;

    public ProtocolSystemConfigurationBean() {

        ProtocolManager manager = ProtocolManager.getInstance();

        this.internalOrganizationalModel = manager.getInternalOrganizationalModel();
        this.externalOrganizationalModel = manager.getExternalOrganizationalModel();

        this.administrativeGroup = manager.getAdministrativeGroup().getDelegateGroup();
    }

    public OrganizationalModel getInternalOrganizationalModel() {
        return internalOrganizationalModel;
    }

    public void setInternalOrganizationalModel(OrganizationalModel internalOrganizationalModel) {
        this.internalOrganizationalModel = internalOrganizationalModel;
    }

    public OrganizationalModel getExternalOrganizationalModel() {
        return externalOrganizationalModel;
    }

    public void setExternalOrganizationalModel(OrganizationalModel externalOrganizationalModel) {
        this.externalOrganizationalModel = externalOrganizationalModel;
    }

    public PersistentGroup getAdministrativeGroup() {
        return administrativeGroup;
    }

    public void setAdministrativeGroup(PersistentGroup administrativeGroup) {
        this.administrativeGroup = administrativeGroup;
    }

}
