package module.protocols.domain;

import jvstm.cps.ConsistencyPredicate;
import module.protocols.domain.util.ProtocolResponsibleType;
import module.protocols.dto.ProtocolSystemConfigurationBean;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class ProtocolManager extends ProtocolManager_Base {

    public static ProtocolManager getInstance() {
        if (Bennu.getInstance().getProtocolManager() == null) {
            return initialize();
        }
        return Bennu.getInstance().getProtocolManager();
    }

    @Atomic
    private static ProtocolManager initialize() {
        if (Bennu.getInstance().getProtocolManager() == null) {
            return new ProtocolManager();
        }
        return Bennu.getInstance().getProtocolManager();
    }

    private ProtocolManager() {
        super();
        setBennu(Bennu.getInstance());
        setCurrentSequenceNumber(0l);
    }

    @ConsistencyPredicate
    protected final boolean checkForDifferentOrganizationalModels() {
        return getInternalOrganizationalModel() == null ? true : getInternalOrganizationalModel() != getExternalOrganizationalModel();
    }

    @Atomic
    public void updateFromBean(ProtocolSystemConfigurationBean bean) {
        setInternalOrganizationalModel(bean.getInternalOrganizationalModel());
        setExternalOrganizationalModel(bean.getExternalOrganizationalModel());
    }

    @Atomic
    public Long getNewProtocolNumber() {
        long number = this.getCurrentSequenceNumber();
        this.setCurrentSequenceNumber(number + 1);
        return number;
    }

    @Atomic
    public void reloadAllCountries() {
        getProtocolsSet().forEach(
                (protocol) -> {
                    protocol.getProtocolResponsibleSet().stream()
                            .filter((responsible) -> responsible.getType() == ProtocolResponsibleType.EXTERNAL)
                            .forEach(ProtocolResponsible::reloadCountry);
                });
    }

    public static Group managers() {
        return Group.managers();
    }

}
