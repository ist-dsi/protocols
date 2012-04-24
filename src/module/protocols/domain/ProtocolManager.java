package module.protocols.domain;

import jvstm.cps.ConsistencyPredicate;
import module.organization.domain.OrganizationalModel;
import myorg.domain.ModuleInitializer;
import myorg.domain.MyOrg;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class ProtocolManager extends ProtocolManager_Base implements ModuleInitializer {

    public static ProtocolManager getInstance() {

	ProtocolManager instance = MyOrg.getInstance().getProtocolManager();

	// Should only happen once
	if (instance == null) {
	    instance = createInstance();
	}

	return instance;
    }

    @Service
    private static ProtocolManager createInstance() {
	return new ProtocolManager();
    }

    private ProtocolManager() {
	super();
	setMyOrg(MyOrg.getInstance());
    }

    @Service
    public void setOrganizationalModels(OrganizationalModel internalOrganizationalModel,
	    OrganizationalModel externalOrganizationalModel) {
	setInternalOrganizationalModel(internalOrganizationalModel);
	setExternalOrganizationalModel(externalOrganizationalModel);
    }

    @ConsistencyPredicate
    protected final boolean checkForDifferentOrganizationalModels() {
	return getInternalOrganizationalModel() == null ? true
		: getInternalOrganizationalModel() != getExternalOrganizationalModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see myorg.domain.ModuleInitializer#init(myorg.domain.MyOrg)
     */
    @Override
    public void init(MyOrg root) {
	throw new RuntimeException("ERROR: This module is still under development, should not be used in"
		+ " production environments. If you are sure about what you are doing, remove"
		+ " the ModuleInitializer from ProtocolManager");
    }
}
