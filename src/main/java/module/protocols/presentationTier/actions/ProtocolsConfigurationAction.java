package module.protocols.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.protocols.domain.ProtocolAuthorizationGroup;
import module.protocols.domain.ProtocolManager;
import module.protocols.dto.ProtocolSystemConfigurationBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.base.BaseAction;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import com.google.common.base.Strings;

@StrutsFunctionality(app = ProtocolsDispatchAction.class, path = "configuration", titleKey = "label.protocolSystem.configure",
        accessGroup = "#managers")
@Mapping(path = "/protocolsConfiguration")
public class ProtocolsConfigurationAction extends BaseAction {

    @EntryPoint
    public ActionForward protocolSystemConfiguration(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        ProtocolSystemConfigurationBean bean = getRenderedObject();
        if (bean == null) {
            bean = new ProtocolSystemConfigurationBean();
        } else {
            ProtocolManager.getInstance().updateFromBean(bean);
        }

        request.setAttribute("configurationBean", bean);

        request.setAttribute("authorizationGroups", ProtocolManager.getInstance().getProtocolAuthorizationGroupsSet());

        return forward("/protocols/protocolSystemConfiguration.jsp");
    }

    public ActionForward reloadCountries(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        ProtocolManager.getInstance().reloadAllCountries();
        return protocolSystemConfiguration(mapping, form, request, response);
    }

    public ActionForward createNewAuthorizationGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        String groupExpression = request.getParameter("expression");

        if (!Strings.isNullOrEmpty(groupExpression)) {
            Group group = Group.parse(groupExpression);
            if (!ProtocolAuthorizationGroup.createGroupWithWriter(group)) {
                setMessage(request, "errorMessage", new ActionMessage("label.protocolSystem.group.alredy.exists"));
            }
        }

        return protocolSystemConfiguration(mapping, form, request, response);
    }

    public ActionForward removeAuthorizationGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        ProtocolAuthorizationGroup group = getDomainObject(request, "OID");

        try {
            group.delete();
        } catch (DomainException e) {
            setMessage(request, "errorMessage", new ActionMessage(e.getKey()));
        }

        return protocolSystemConfiguration(mapping, form, request, response);
    }

    public ActionForward configureAuthorizationGroup(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        ProtocolAuthorizationGroup group = getDomainObject(request, "group");
        request.setAttribute("group", group);
        return forward("/protocols/configureAuthorizationGroup.jsp");
    }

    public ActionForward addReaderToGroup(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        ProtocolAuthorizationGroup group = getDomainObject(request, "group");
        String expression = request.getParameter("expression");
        if (!Strings.isNullOrEmpty(expression)) {
            Group readerGroup = Group.parse(expression);
            group.grant(readerGroup);
        }
        return configureAuthorizationGroup(mapping, form, request, response);
    }

    public ActionForward revoke(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        ProtocolAuthorizationGroup group = getDomainObject(request, "group");
        PersistentGroup reader = getDomainObject(request, "reader");
        group.revoke(reader);
        return configureAuthorizationGroup(mapping, form, request, response);
    }

    private void setMessage(HttpServletRequest request, String error, ActionMessage actionMessage) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add(error, actionMessage);
        saveMessages(request, actionMessages);
    }
}
