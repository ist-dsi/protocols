/**
 * 
 */
package module.protocols.presentationTier.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.organization.domain.Unit;
import module.protocols.domain.Protocol;
import module.protocols.domain.ProtocolManager;
import module.protocols.domain.ProtocolResponsible;
import module.protocols.domain.util.ProtocolResponsibleType;
import module.protocols.dto.ProtocolCreationBean;
import module.protocols.dto.ProtocolCreationBean.ProtocolResponsibleBean;
import module.protocols.dto.ProtocolHistoryBean;
import module.protocols.dto.ProtocolSearchBean;
import module.protocols.dto.ProtocolSystemConfigurationBean;
import myorg.domain.RoleType;
import myorg.domain.VirtualHost;
import myorg.domain.contents.ActionNode;
import myorg.domain.contents.Node;
import myorg.domain.groups.Role;
import myorg.presentationTier.actions.ContextBaseAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.functionalities.CreateNodeAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.CollectionUtils;
import pt.utl.ist.fenix.tools.util.Predicate;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
@Mapping(path = "/protocols")
public class ProtocolsDispatchAction extends ContextBaseAction {

    @CreateNodeAction(bundle = "PROTOCOLS_RESOURCES", key = "add.node.manage.protocols", groupKey = "label.module.protocols")
    public final ActionForward createProtocolsNode(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {

	final VirtualHost virtualHost = getDomainObject(request, "virtualHostToManageId");

	final Node parentOfNodes = getDomainObject(request, "parentOfNodesToManageId");

	final ActionNode topActionNode = ActionNode.createActionNode(virtualHost, parentOfNodes, "/protocols", "firstPage",
		"resources.ProtocolsResources", "label.module.protocols", Role.getRole(RoleType.MANAGER));

	ActionNode.createActionNode(virtualHost, topActionNode, "/protocols", "showProtocols", "resources.ProtocolsResources",
		"label.protocols.show", Role.getRole(RoleType.MANAGER));

	ActionNode.createActionNode(virtualHost, topActionNode, "/protocols", "searchProtocols", "resources.ProtocolsResources",
		"label.protocols.search", Role.getRole(RoleType.MANAGER));

	ActionNode.createActionNode(virtualHost, topActionNode, "/protocols", "showAlerts", "resources.ProtocolsResources",
		"label.protocols.alerts", Role.getRole(RoleType.MANAGER));

	ActionNode.createActionNode(virtualHost, topActionNode, "/protocols", "prepareCreateProtocolData",
		"resources.ProtocolsResources", "label.protocols.create", Role.getRole(RoleType.MANAGER));

	// System configuration. Should only be accessed by the administrative
	// group
	ActionNode.createActionNode(virtualHost, topActionNode, "/protocols", "protocolSystemConfiguration",
		"resources.ProtocolsResources", "label.protocolSystem.configure", ProtocolManager.getInstance()
			.getAdministrativeGroup());

	return forwardToMuneConfiguration(request, virtualHost, topActionNode);
    }

    public ActionForward firstPage(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	return forward(request, "/protocols/firstPage.jsp");
    }

    public ActionForward showProtocols(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	List<Protocol> protocols = ProtocolManager.getInstance().getProtocols();

	request.setAttribute("protocols", protocols);

	return forward(request, "/protocols/showProtocols.jsp");
    }

    public ActionForward showAlerts(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	setOrganizationalModels(request);

	List<Protocol> allProtocols = ProtocolManager.getInstance().getProtocols();

	List<Protocol> almostExpiredProtocols = CollectionUtils.filter(allProtocols, new Predicate<Protocol>() {

	    @Override
	    public boolean evaluate(Protocol protocol) {
		if (!protocol.isActive())
		    return false;
		LocalDate endDate = protocol.getLastProtocolHistory().getEndDate();
		return endDate == null ? false : endDate.minusMonths(1).isBefore(new LocalDate());
	    }
	});

	List<Protocol> nullEndDateProtocols = CollectionUtils.filter(allProtocols, new Predicate<Protocol>() {

	    @Override
	    public boolean evaluate(Protocol protocol) {
		return protocol.isActive() && protocol.getCurrentProtocolHistory().getEndDate() == null;
	    }
	});

	request.setAttribute("almostExpiredProtocols", almostExpiredProtocols);
	request.setAttribute("nullEndDateProtocols", nullEndDateProtocols);

	return forward(request, "/protocols/showAlerts.jsp");
    }

    public ActionForward protocolSystemConfiguration(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	ProtocolSystemConfigurationBean bean = getRenderedObject();

	if (bean == null)
	    bean = new ProtocolSystemConfigurationBean();
	else
	    ProtocolManager.getInstance().updateFromBean(bean);

	request.setAttribute("configurationBean", bean);

	return forward(request, "/protocols/protocolSystemConfiguration.jsp");
    }

    public ActionForward viewProtocolDetails(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	String OID = request.getParameter("OID");

	return viewProtocolDetailsFromOID(request, OID);
    }

    private ActionForward viewProtocolDetailsFromOID(final HttpServletRequest request, String OID) {

	Protocol protocol = Protocol.fromExternalId(OID);

	request.setAttribute("protocol", protocol);

	List<ProtocolResponsible> responsibles = protocol.getProtocolResponsible();

	List<ProtocolResponsible> internalResponsibles = CollectionUtils.filter(responsibles,
		new Predicate<ProtocolResponsible>() {

		    @Override
		    public boolean evaluate(ProtocolResponsible responsible) {
			return responsible.getType() == ProtocolResponsibleType.INTERNAL;
		    }

		});

	List<ProtocolResponsible> externalResponsibles = CollectionUtils.filter(responsibles,
		new Predicate<ProtocolResponsible>() {

		    @Override
		    public boolean evaluate(ProtocolResponsible responsible) {
			return responsible.getType() == ProtocolResponsibleType.EXTERNAL;
		    }

		});

	request.setAttribute("internalResponsibles", internalResponsibles);
	request.setAttribute("externalResponsibles", externalResponsibles);

	return forward(request, "/protocols/viewProtocolDetails.jsp");
    }

    /*
     * Searching
     */

    public ActionForward searchProtocols(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	ProtocolSearchBean bean = getRenderedObject();

	if (bean != null) {

	    List<Protocol> filteredProtocols = CollectionUtils.filter(ProtocolManager.getInstance().getProtocols(), bean);

	    request.setAttribute("searchResults", filteredProtocols);

	} else {
	    bean = new ProtocolSearchBean();
	}

	request.setAttribute("protocolSearch", bean);

	return forward(request, "/protocols/searchProtocols.jsp");
    }

    /*
     * Protocol History edition
     */

    public ActionForward prepareEditProtocolHistory(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	Protocol protocol = Protocol.fromExternalId(request.getParameter("OID"));

	request.setAttribute("protocolHistory", new ProtocolHistoryBean(protocol.getCurrentProtocolHistory()));

	return forward(request, "/protocols/editProtocolHistory.jsp");
    }

    public ActionForward editProtocolHistory(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	ProtocolHistoryBean bean = getRenderedObject();

	if (!validateDates(bean.getBeginDate(), bean.getEndDate(), request)) {
	    return forward(request, "/protocols/editProtocolHistory.jsp");
	}

	bean.getProtocolHistory().editProtocolHistory(bean.getBeginDate(), bean.getEndDate());

	setMessage(request, "success", new ActionMessage("label.protocolHistory.editSuccessful"));

	return showAlerts(mapping, form, request, response);
    }

    /*
     * Renewal Process
     */

    public ActionForward prepareRenewProtocol(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	String protocolOID = request.getParameter("protocolOID");

	Protocol protocol = Protocol.fromExternalId(protocolOID);

	ProtocolHistoryBean bean = new ProtocolHistoryBean(protocol);

	request.setAttribute("protocol", bean);

	return forward(request, "/protocols/renewProtocol.jsp");
    }

    public ActionForward renewProtocol(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	ProtocolHistoryBean bean = getRenderedObject();

	bean.getProtocol().renewFor(bean.getDuration(), bean.getRenewTime());

	setMessage(request, "success", new ActionMessage("label.procotols.renew.success"));

	return showAlerts(mapping, form, request, response);
    }

    private void setOrganizationalModels(HttpServletRequest request) {
	request.setAttribute("internalOrganizationalModel", ProtocolManager.getInstance().getInternalOrganizationalModel());
	request.setAttribute("externalOrganizationalModel", ProtocolManager.getInstance().getExternalOrganizationalModel());
    }

    /*
     * Protocol Creation
     */

    public ActionForward prepareCreateProtocolData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("protocolBean", new ProtocolCreationBean());

	return forward(request, "/protocols/prepareCreateProtocolData.jsp");
    }

    // Protocol data inserted, proceed to create internal responsibles
    public ActionForward prepareCreateInternalResponsibles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ProtocolCreationBean protocolBean = getRenderedObject();
	RenderUtils.invalidateViewState();

	request.setAttribute("protocolBean", protocolBean);

	if (isCancelled(request)) {
	    return showProtocols(mapping, form, request, response);
	}

	if (protocolBean.isProtocolNumberValid()) {
	    if (validateDates(protocolBean.getBeginDate(), protocolBean.getEndDate(), request)) {
		return forward(request, "/protocols/prepareCreateInternalResponsibles.jsp");
	    } else {
		return forward(request, "/protocols/prepareCreateProtocolData.jsp");
	    }
	} else {
	    validateDates(protocolBean.getBeginDate(), protocolBean.getEndDate(), request);
	    setMessage(request, "errorMessage", new ActionMessage("error.protocol.number.alreadyExists"));
	    return forward(request, "/protocols/prepareCreateProtocolData.jsp");
	}
    }

    // Internal data inserted, proceed to permission setting
    public ActionForward prepareCreateExternalResponsibles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ProtocolCreationBean protocolBean = getRenderedObject();
	RenderUtils.invalidateViewState();

	request.setAttribute("protocolBean", protocolBean);

	if (request.getParameter("back") != null) {
	    return forward(request, "/protocols/prepareCreateProtocolData.jsp");
	}

	if (!protocolBean.internalResponsiblesCorrect()) {
	    setMessage(request, "errorMessage", new ActionMessage("error.protocols.invalidInternalResponsibles"));
	    return forward(request, "/protocols/prepareCreateInternalResponsibles.jsp");
	}

	return forward(request, "/protocols/prepareCreateExternalResponsibles.jsp");
    }

    // External data inserted, proceed to permission setting
    public ActionForward prepareDefineProtocolPermissions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ProtocolCreationBean protocolBean = getRenderedObject();
	RenderUtils.invalidateViewState();

	request.setAttribute("protocolBean", protocolBean);

	if (request.getParameter("back") != null) {
	    return forward(request, "/protocols/prepareCreateInternalResponsibles.jsp");
	}

	if (!protocolBean.externalResponsiblesCorrect()) {
	    setMessage(request, "errorMessage", new ActionMessage("error.protocols.invalidExternalResponsibles"));
	    return forward(request, "/protocols/prepareCreateExternalResponsibles.jsp");
	}

	return forward(request, "/protocols/prepareDefineProtocolPermissions.jsp");
    }

    // All data inserted, create protocol
    public ActionForward createProtocol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ProtocolCreationBean protocolBean = getRenderedObject();
	RenderUtils.invalidateViewState();

	request.setAttribute("protocolBean", protocolBean);

	if (request.getParameter("back") != null) {
	    return forward(request, "/protocols/prepareCreateExternalResponsibles.jsp");
	}

	if (!protocolBean.permissionsCorrectlyDefined()) {
	    setMessage(request, "errorMessage", new ActionMessage("error.protocols.invalidProtocolPermissions"));
	    return forward(request, "/protocols/prepareDefineProtocolPermissions.jsp");
	}

	Protocol protocol = Protocol.createProtocol(protocolBean);

	setMessage(request, "success", new ActionMessage("label.protocols.successCreate"));

	return viewProtocolDetailsFromOID(request, protocol.getExternalId());
    }

    public ActionForward updateBean(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	ProtocolCreationBean protocolBean = getRenderedObject();
	RenderUtils.invalidateViewState();
	request.setAttribute("protocolBean", protocolBean);

	boolean internal = true;

	if (request.getParameter("insertInternalUnit") != null && protocolBean.getNewUnit() != null) {

	    protocolBean.addInternalResponsible(new ProtocolResponsibleBean(protocolBean.getNewUnit()));
	    protocolBean.setNewUnit(null);

	} else if (request.getParameter("insertPersonInUnit") != null && protocolBean.getNewPerson() != null) {

	    Unit unit = Unit.fromExternalId(request.getParameter("unitOID"));

	    protocolBean.getBeanForUnit(unit).addResponsible(protocolBean.getNewPerson());

	    protocolBean.setNewPerson(null);

	} else if (request.getParameter("removePersonInUnit") != null && protocolBean.getNewPerson() != null) {

	    Unit unit = Unit.fromExternalId(request.getParameter("unitOID"));

	    protocolBean.getBeanForUnit(unit).removeResponsible(protocolBean.getNewPerson());

	    protocolBean.setNewPerson(null);

	} else if (request.getParameter("removeUnit") != null) {

	    Unit unit = Unit.fromExternalId(request.getParameter("unitOID"));

	    protocolBean.removeUnit(unit);

	} else if (request.getParameter("insertExternalUnit") != null) {

	    protocolBean.addExternalResponsible(new ProtocolResponsibleBean(protocolBean.getNewUnit()));
	    protocolBean.setNewUnit(null);

	    internal = false;

	} else if (request.getParameter("insertPersonInExternalUnit") != null && protocolBean.getNewPerson() != null) {

	    Unit unit = Unit.fromExternalId(request.getParameter("unitOID"));

	    protocolBean.getBeanForUnit(unit).addResponsible(protocolBean.getNewPerson());

	    protocolBean.setNewPerson(null);

	    internal = false;

	}

	return forward(request, internal ? "/protocols/prepareCreateInternalResponsibles.jsp"
		: "/protocols/prepareCreateExternalResponsibles.jsp");
    }

    /*
     * Helper methods
     */

    private boolean validateDates(LocalDate beginDate, LocalDate endDate, HttpServletRequest request) {
	if (beginDate != null && endDate != null && endDate.isBefore(beginDate)) {
	    setMessage(request, "errorMessage", new ActionMessage("error.protocols.dates.notContinuous"));
	    return false;
	}
	return true;
    }

    private void setMessage(HttpServletRequest request, String error, ActionMessage actionMessage) {
	ActionMessages actionMessages = getMessages(request);
	actionMessages.add(error, actionMessage);
	saveMessages(request, actionMessages);
    }

}
