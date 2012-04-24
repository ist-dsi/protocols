package module.protocols.domain.util;

import module.organization.domain.OrganizationalModel;
import module.protocols.domain.ProtocolManager;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public enum ProtocolResponsibleType {

    INTERNAL(ProtocolManager.getInstance().getInternalOrganizationalModel()), EXTERNAL(ProtocolManager.getInstance()
	    .getExternalOrganizationalModel());

    private OrganizationalModel model;

    private ProtocolResponsibleType(OrganizationalModel model) {
	this.model = model;
    }

    public OrganizationalModel getOrganizationalModel() {
	return model;
    }

}
