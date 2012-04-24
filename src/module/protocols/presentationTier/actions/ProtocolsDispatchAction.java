/**
 * 
 */
package module.protocols.presentationTier.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.protocols.domain.Protocol;
import module.protocols.domain.ProtocolHistory;
import module.protocols.domain.ProtocolManager;
import module.protocols.domain.ProtocolResponsible;
import module.protocols.domain.util.ProtocolResponsibleType;
import module.protocols.dto.OrganizationalModelBean;
import module.protocols.dto.ProtocolCreationBean;
import module.protocols.dto.ProtocolCreationBean.ProtocolResponsibleBean;
import module.protocols.dto.ProtocolHistoryBean;
import module.protocols.dto.ProtocolSearchBean;
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

	ActionNode.createActionNode(virtualHost, topActionNode, "/protocols", "prepareToDefineOrganizationalModel",
		"resources.ProtocolsResources", "label.organizationalModel.define", Role.getRole(RoleType.MANAGER));

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
		LocalDate endDate = protocol.getCurrentProtocolHistory().getEndDate();
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

    public ActionForward prepareToDefineOrganizationalModel(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	OrganizationalModelBean bean = getRenderedObject();

	if (bean == null)
	    bean = new OrganizationalModelBean();

	request.setAttribute("organizationalModelBean", bean);

	return forward(request, "/protocols/defineOrganizationalModel.jsp");
    }

    public ActionForward defineOrganizationalModel(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	OrganizationalModelBean bean = getRenderedObject();

	if (bean != null) {

	    if (bean.getInternalOrganizationalModel() != null
		    && bean.getInternalOrganizationalModel().equals(bean.getExternalOrganizationalModel())) {
		setError(request, "errorMessage", new ActionMessage("label.organizationalModel.equalModels"));
		return prepareToDefineOrganizationalModel(mapping, form, request, response);
	    } else {

		ProtocolManager.getInstance().setOrganizationalModels(bean.getInternalOrganizationalModel(),
			bean.getExternalOrganizationalModel());
	    }
	}

	return showAlerts(mapping, form, request, response);
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

	request.setAttribute("protocolHistory", protocol.getCurrentProtocolHistory());

	return forward(request, "/protocols/editProtocolHistory.jsp");
    }

    public ActionForward editProtocolHistory(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	ProtocolHistory protocolHistory = getRenderedObject();
	LocalDate beginDate = protocolHistory.getBeginDate(), endDate = protocolHistory.getEndDate();

	if (endDate != null && beginDate.isAfter(endDate)) {
	    return invalidProtocolHistoryDates(mapping, form, request, response);
	}

	protocolHistory.editProtocolHistory(beginDate, endDate);
	addMessage(request, "label.protocolHistory.editSuccessful");

	return showAlerts(mapping, form, request, response);
    }

    public ActionForward invalidProtocolHistoryDates(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	addMessage(request, "label.protocolHistory.invalidDates");

	return prepareEditProtocolHistory(mapping, form, request, response);
    }

    /*
     * Renewal Process
     */

    public ActionForward prepareRenewProtocol(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	String protocolOID = request.getParameter("OID");

	Protocol protocol = Protocol.fromExternalId(protocolOID);

	ProtocolHistoryBean bean = new ProtocolHistoryBean(protocol);

	request.setAttribute("protocol", bean);

	return forward(request, "/protocols/renewProtocol.jsp");
    }

    public ActionForward renewProtocol(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	ProtocolHistoryBean bean = getRenderedObject();

	bean.getProtocol().renewFor(bean.getDuration(), bean.getRenewTime());

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
	    if (validateDates(protocolBean, request)) {
		return forward(request, "/protocols/prepareCreateInternalResponsibles.jsp");
	    } else {
		return forward(request, "/protocols/prepareCreateProtocolData.jsp");
	    }
	} else {
	    validateDates(protocolBean, request);
	    setError(request, "errorMessage", new ActionMessage("error.protocol.number.alreadyExists"));
	    return forward(request, "/protocols/prepareCreateInternalResponsibles.jsp");
	}
    }

    public ActionForward updateBean(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	ProtocolCreationBean protocolBean = getRenderedObject();
	RenderUtils.invalidateViewState();
	request.setAttribute("protocolBean", protocolBean);

	boolean internal = true;

	if (request.getParameter("insertInternalUnit") != null) {
	    System.out.println("Inserting unit...");

	    protocolBean.addInternalResponsible(new ProtocolResponsibleBean(protocolBean.getNewUnit()));
	    protocolBean.setNewUnit(null);

	    internal = true;
	}

	return forward(request, internal ? "/protocols/prepareCreateInternalResponsibles.jsp"
		: "/protocols/prepareCreateExternalResponsibles.jsp");
    }

    // Internal responsibles inserted, proceed to external responsibles
    public ActionForward prepareCreateExternalResponsibles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ProtocolCreationBean protocolBean = getRenderedObject();
	RenderUtils.invalidateViewState();

	request.setAttribute("protocolBean", protocolBean);

	if (isCancelled(request)) {
	    return showProtocols(mapping, form, request, response);
	}

	if (!protocolBean.internalResponsiblesCorrect()) {
	    setError(request, "errorMessage", new ActionMessage("error.protocols.invalidInternalResponsibles"));
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

	if (isCancelled(request)) {
	    return showProtocols(mapping, form, request, response);
	}

	if (!protocolBean.externalResponsiblesCorrect()) {
	    setError(request, "errorMessage", new ActionMessage("error.protocols.invalidExternalResponsibles"));
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

	if (isCancelled(request)) {
	    return showProtocols(mapping, form, request, response);
	}

	if (!protocolBean.permissionsCorrectlyDefined()) {
	    setError(request, "errorMessage", new ActionMessage("error.protocols.invalidProtocolPermissions"));
	    return forward(request, "/protocols/prepareDefineProtocolPermissions.jsp");
	}

	Protocol protocol = Protocol.createProtocol(protocolBean);

	addMessage(request, "label.protocols.successCreate");

	return viewProtocolDetailsFromOID(request, protocol.getExternalId());
    }

    /*
     * Helper methods
     */

    private boolean validateDates(ProtocolCreationBean protocolFactory, HttpServletRequest request) {
	if (protocolFactory.getBeginDate() != null && protocolFactory.getEndDate() != null) {
	    if (!protocolFactory.getBeginDate().isBefore(protocolFactory.getEndDate())) {
		setError(request, "errorMessage", new ActionMessage("error.protocols.dates.notContinuous"));
		return false;
	    }
	}
	return true;
    }

    private void setError(HttpServletRequest request, String error, ActionMessage actionMessage) {
	ActionMessages actionMessages = getMessages(request);
	actionMessages.add(error, actionMessage);
	saveMessages(request, actionMessages);
    }

}
