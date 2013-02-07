package module.protocols.domain.util;

import module.organization.domain.OrganizationalModel;
import module.protocols.domain.ProtocolManager;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public enum ProtocolResponsibleType {

    INTERNAL() {
        @Override
        public OrganizationalModel getOrganizationalModel() {
            return ProtocolManager.getInstance().getInternalOrganizationalModel();
        }
    },

    EXTERNAL() {
        @Override
        public OrganizationalModel getOrganizationalModel() {
            return ProtocolManager.getInstance().getExternalOrganizationalModel();
        }
    };

    abstract public OrganizationalModel getOrganizationalModel();

}
