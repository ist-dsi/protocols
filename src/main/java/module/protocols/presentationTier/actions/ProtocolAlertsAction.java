package module.protocols.presentationTier.actions;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.protocols.domain.Protocol;
import module.protocols.domain.ProtocolManager;
import module.protocols.dto.ProtocolHistoryBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.base.BaseAction;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

@StrutsFunctionality(app = ProtocolsDispatchAction.class, path = "alerts", titleKey = "label.protocols.alerts",
        accessGroup = "protocolCreators | #managers")
@Mapping(path = "/protocolAlerts")
public class ProtocolAlertsAction extends BaseAction {

    @EntryPoint
    public ActionForward showAlerts(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final User user = Authenticate.getUser();

        setOrganizationalModels(request);

        Collection<Protocol> allProtocols = ProtocolManager.getInstance().getProtocolsSet();

        Collection<Protocol> almostExpiredProtocols = Collections2.filter(allProtocols, new Predicate<Protocol>() {

            @Override
            public boolean apply(Protocol protocol) {
                if (!protocol.isActive() || !protocol.canBeWrittenByUser(user)) {
                    return false;
                }
                LocalDate endDate = protocol.getLastProtocolHistory().getEndDate();
                return endDate == null ? false : endDate.isBefore(new LocalDate().plusMonths(2).withDayOfMonth(1).minusDays(1));
            }
        });

        Collection<Protocol> nullEndDateProtocols = Collections2.filter(allProtocols, new Predicate<Protocol>() {

            @Override
            public boolean apply(Protocol protocol) {
                return protocol.isActive() && protocol.canBeWrittenByUser(user)
                        && protocol.getCurrentProtocolHistory().getEndDate() == null;
            }
        });

        request.setAttribute("almostExpiredProtocols", almostExpiredProtocols);
        request.setAttribute("nullEndDateProtocols", nullEndDateProtocols);

        return forward("/protocols/showAlerts.jsp");
    }

    public ActionForward prepareRenewProtocol(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        String protocolOID = request.getParameter("protocolOID");

        Protocol protocol = FenixFramework.getDomainObject(protocolOID);

        ProtocolHistoryBean bean = new ProtocolHistoryBean(protocol);

        request.setAttribute("protocol", bean);

        return forward("/protocols/renewProtocol.jsp");
    }

    public ActionForward renewProtocol(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        ProtocolHistoryBean bean = getRenderedObject();

        bean.getProtocol().renewFor(bean.getDuration(), bean.getRenewTime());

        setMessage(request, "success", new ActionMessage("label.procotols.renew.success"));

        return showAlerts(mapping, form, request, response);
    }

    public ActionForward prepareEditProtocolHistory(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        Protocol protocol = getDomainObject(request, "OID");

        request.setAttribute("protocolHistory", new ProtocolHistoryBean(protocol.getCurrentProtocolHistory()));

        return forward("/protocols/editProtocolHistory.jsp");
    }

    public ActionForward editProtocolHistory(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        ProtocolHistoryBean bean = getRenderedObject();

        if (!validateDates(bean.getBeginDate(), bean.getEndDate(), request)) {
            return forward("/protocols/editProtocolHistory.jsp");
        }

        bean.getProtocolHistory().editProtocolHistory(bean.getBeginDate(), bean.getEndDate());

        setMessage(request, "success", new ActionMessage("label.protocolHistory.editSuccessful"));

        return showAlerts(mapping, form, request, response);
    }

    private void setMessage(HttpServletRequest request, String error, ActionMessage actionMessage) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add(error, actionMessage);
        saveMessages(request, actionMessages);
    }

    private void setOrganizationalModels(HttpServletRequest request) {
        request.setAttribute("internalOrganizationalModel", ProtocolManager.getInstance().getInternalOrganizationalModel());
        request.setAttribute("externalOrganizationalModel", ProtocolManager.getInstance().getExternalOrganizationalModel());
    }

    private boolean validateDates(LocalDate beginDate, LocalDate endDate, HttpServletRequest request) {
        if (beginDate != null && endDate != null && endDate.isBefore(beginDate)) {
            setMessage(request, "errorMessage", new ActionMessage("error.protocols.dates.notContinuous"));
            return false;
        }
        return true;
    }
}
