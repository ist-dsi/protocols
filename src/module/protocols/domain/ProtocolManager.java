package module.protocols.domain;

import java.util.ArrayList;
import java.util.List;

import jvstm.cps.ConsistencyPredicate;
import module.protocols.dto.ProtocolSystemConfigurationBean;
import myorg.domain.MyOrg;
import myorg.domain.RoleType;
import myorg.domain.groups.PersistentGroup;
import myorg.domain.groups.Role;
import myorg.domain.groups.UnionGroup;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class ProtocolManager extends ProtocolManager_Base {

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

	ProtocolManager manager = new ProtocolManager();
	manager.setAdministrativeGroup(Role.getRole(RoleType.MANAGER));

	return manager;
    }

    private ProtocolManager() {
	super();
	setMyOrg(MyOrg.getInstance());
    }

    @ConsistencyPredicate
    protected final boolean checkForDifferentOrganizationalModels() {
	return getInternalOrganizationalModel() == null ? true
		: getInternalOrganizationalModel() != getExternalOrganizationalModel();
    }

    public static PersistentGroup createGroupFor(List<PersistentGroup> groups) {

	PersistentGroup administrativeGroup = getInstance().getAdministrativeGroup();

	if (administrativeGroup != null) {
	    groups = new ArrayList<PersistentGroup>(groups);
	    groups.add(administrativeGroup);
	}

	return new UnionGroup(groups);
    }

    /**
     * @param bean
     */
    @Service
    public void updateFromBean(ProtocolSystemConfigurationBean bean) {
	setInternalOrganizationalModel(bean.getInternalOrganizationalModel());
	setExternalOrganizationalModel(bean.getExternalOrganizationalModel());
	setAdministrativeGroup(bean.getAdministrativeGroup());
    }
}
