package module.protocols.domain;

import java.util.ArrayList;
import java.util.List;

import jvstm.cps.ConsistencyPredicate;
import module.protocols.dto.ProtocolSystemConfigurationBean;
import myorg.domain.MyOrg;
import myorg.domain.groups.PersistentGroup;
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
	manager.setAdministrativeGroup(new ProtocolAdministrativeGroup());
	manager.setCreatorsGroup(new UnionGroup(manager.getAdministrativeGroup()));

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

    public static PersistentGroup createReaderGroup(List<PersistentGroup> groups) {

	PersistentGroup administrativeGroup = getInstance().getAdministrativeGroup();

	List<PersistentGroup> actualGroups = new ArrayList<PersistentGroup>(groups);
	actualGroups.add(administrativeGroup);

	return UnionGroup.getOrCreateUnionGroup(actualGroups.toArray(new PersistentGroup[0]));
    }

    public static PersistentGroup createWriterGroup(ProtocolAuthorizationGroup group) {

	ProtocolAdministrativeGroup adminGroup = getInstance().getAdministrativeGroup();

	return UnionGroup.getOrCreateUnionGroup(adminGroup, group.getAuthorizedWriterGroup());

    }

    /**
     * @param bean
     */
    @Service
    public void updateFromBean(ProtocolSystemConfigurationBean bean) {
	setInternalOrganizationalModel(bean.getInternalOrganizationalModel());
	setExternalOrganizationalModel(bean.getExternalOrganizationalModel());
	getAdministrativeGroup().setDelegateGroup(bean.getAdministrativeGroup());
    }
}
