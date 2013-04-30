package module.protocols.domain;

import jvstm.cps.ConsistencyPredicate;
import module.protocols.domain.util.ProtocolResponsibleType;
import module.protocols.dto.ProtocolSystemConfigurationBean;
import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.groups.UnionGroup;
import pt.ist.fenixframework.Atomic;

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

    @Atomic
    private static ProtocolManager createInstance() {

        ProtocolManager manager = new ProtocolManager();
        manager.setAdministrativeGroup(new ProtocolAdministrativeGroup());
        manager.setCreatorsGroup(new UnionGroup(manager.getAdministrativeGroup()));

        return manager;
    }

    private ProtocolManager() {
        super();
        setMyOrg(MyOrg.getInstance());
        setCurrentSequenceNumber(0l);
    }

    @ConsistencyPredicate
    protected final boolean checkForDifferentOrganizationalModels() {
        return getInternalOrganizationalModel() == null ? true : getInternalOrganizationalModel() != getExternalOrganizationalModel();
    }

    /**
     * @param bean
     */
    @Atomic
    public void updateFromBean(ProtocolSystemConfigurationBean bean) {
        setInternalOrganizationalModel(bean.getInternalOrganizationalModel());
        setExternalOrganizationalModel(bean.getExternalOrganizationalModel());
        getAdministrativeGroup().setDelegateGroup(bean.getAdministrativeGroup());
    }

    @Atomic
    public Long getNewProtocolNumber() {
        long number = this.getCurrentSequenceNumber();
        this.setCurrentSequenceNumber(number + 1);
        return number;
    }

    @Atomic
    public void reloadAllCountries() {
        for (Protocol protocol : getProtocols()) {
            for (ProtocolResponsible responsible : protocol.getProtocolResponsible()) {
                if (responsible.getType() == ProtocolResponsibleType.EXTERNAL) {
                    responsible.reloadCountry();
                }
            }
        }
    }

    @Deprecated
    public java.util.Set<module.protocols.domain.ProtocolAuthorizationGroup> getProtocolAuthorizationGroups() {
        return getProtocolAuthorizationGroupsSet();
    }

    @Deprecated
    public java.util.Set<module.protocols.domain.Protocol> getProtocols() {
        return getProtocolsSet();
    }

}
