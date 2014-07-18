/**
 * 
 */
package module.protocols.presentationTier.actions;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.organization.domain.Person;
import module.organization.domain.Unit;
import module.protocols.domain.Protocol;
import module.protocols.domain.ProtocolCreatorsGroup;
import module.protocols.domain.ProtocolFile;
import module.protocols.domain.ProtocolManager;
import module.protocols.domain.ProtocolResponsible;
import module.protocols.domain.ProtocolsDomainException;
import module.protocols.domain.util.ProtocolResponsibleType;
import module.protocols.dto.ProtocolCreationBean;
import module.protocols.dto.ProtocolCreationBean.ProtocolResponsibleBean;
import module.protocols.dto.ProtocolFileUploadBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.presentationTier.actions.BaseAction;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsApplication;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.io.ByteStreams;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
@StrutsApplication(bundle = "ProtocolsResources", path = "protocols", titleKey = "label.module.protocols", accessGroup = "logged")
@StrutsFunctionality(app = ProtocolsDispatchAction.class, path = "list", titleKey = "label.protocols.show")
@Mapping(path = "/protocols")
public class ProtocolsDispatchAction extends BaseAction {

    @EntryPoint
    public ActionForward showProtocols(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final User currentUser = Authenticate.getUser();

        List<Protocol> protocols =
                ProtocolManager.getInstance().getProtocolsSet().stream()
                        .filter((protocol) -> protocol.canBeReadByUser(currentUser)).filter(Protocol::isActive)
                        .collect(Collectors.toList());

        request.setAttribute("protocols", protocols);
        request.setAttribute("creator", ProtocolCreatorsGroup.get().isMember(currentUser));

        return forward("/protocols/showProtocols.jsp");
    }

    public ActionForward viewProtocolDetails(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        String OID = request.getParameter("OID");

        return viewProtocolDetailsFromOID(request, OID);
    }

    private ActionForward viewProtocolDetailsFromOID(final HttpServletRequest request, String OID) {

        User currentUser = Authenticate.getUser();

        Protocol protocol = FenixFramework.getDomainObject(OID);

        if (!protocol.canBeReadByUser(currentUser)) {
            return showProtocols(null, null, request, null);
        }

        request.setAttribute("protocol", protocol);

        Collection<ProtocolResponsible> responsibles = protocol.getProtocolResponsibleSet();

        Collection<ProtocolResponsible> internalResponsibles =
                Collections2.filter(responsibles, new Predicate<ProtocolResponsible>() {

                    @Override
                    public boolean apply(ProtocolResponsible responsible) {
                        return responsible.getType() == ProtocolResponsibleType.INTERNAL;
                    }

                });

        Collection<ProtocolResponsible> externalResponsibles =
                Collections2.filter(responsibles, new Predicate<ProtocolResponsible>() {

                    @Override
                    public boolean apply(ProtocolResponsible responsible) {
                        return responsible.getType() == ProtocolResponsibleType.EXTERNAL;
                    }

                });

        request.setAttribute("internalResponsibles", internalResponsibles);
        request.setAttribute("externalResponsibles", externalResponsibles);

        if (protocol.canFilesBeReadByUser(currentUser)) {
            request.setAttribute("protocolFiles", protocol.getProtocolFileSet());
        }

        request.setAttribute("canBeWritten", protocol.canBeWrittenByUser(currentUser));

        return forward("/protocols/viewProtocolDetails.jsp");
    }

    public ActionForward uploadProtocolFile(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {

        ProtocolFileUploadBean bean = getRenderedObject();

        if (bean == null) {

            Protocol protocol = getDomainObject(request, "OID");

            if (!protocol.canBeWrittenByUser(Authenticate.getUser())) {
                throw ProtocolsDomainException.unauthorized();
            }

            bean = new ProtocolFileUploadBean(protocol);

            request.setAttribute("file", bean);

            return forward("/protocols/uploadProtocolFile.jsp");

        } else {
            if (bean.getInputStream() != null) {
                bean.getProtocol().uploadFile(bean.getFilename(), ByteStreams.toByteArray(bean.getInputStream()));
                setMessage(request, "success", new ActionMessage("label.protocols.fileUpload.success"));
            }
            return viewProtocolDetailsFromOID(request, bean.getProtocol().getExternalId());
        }

    }

    public ActionForward removeProtocolFile(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        Protocol protocol = getDomainObject(request, "protocol");

        if (!protocol.canBeWrittenByUser(Authenticate.getUser())) {
            throw ProtocolsDomainException.unauthorized();
        }

        ProtocolFile file = getDomainObject(request, "file");
        file.delete();

        setMessage(request, "success", new ActionMessage("label.protocols.fileRemoval.success"));

        return viewProtocolDetailsFromOID(request, protocol.getExternalId());

    }

    public ActionForward prepareEditProtocolData(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        Protocol protocol = getDomainObject(request, "OID");

        if (!protocol.canBeWrittenByUser(Authenticate.getUser())) {
            throw ProtocolsDomainException.unauthorized();
        }

        request.setAttribute("protocolBean", new ProtocolCreationBean(protocol));

        return forward("/protocols/prepareEditProtocolData.jsp");
    }

    public ActionForward prepareEditInternalResponsibles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolCreationBean protocolBean = getRenderedObject();
        RenderUtils.invalidateViewState();

        request.setAttribute("protocolBean", protocolBean);

        if (protocolBean.isProtocolNumberValid()) {
            if (validateDates(protocolBean.getBeginDate(), protocolBean.getEndDate(), request)) {
                return forward("/protocols/prepareEditInternalResponsibles.jsp");
            } else {
                return forward("/protocols/prepareEditProtocolData.jsp");
            }
        } else {
            validateDates(protocolBean.getBeginDate(), protocolBean.getEndDate(), request);
            setMessage(request, "errorMessage", new ActionMessage("error.protocol.number.alreadyExists"));
            return forward("/protocols/prepareEditProtocolData.jsp");
        }
    }

    public ActionForward prepareEditExternalResponsibles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolCreationBean protocolBean = getRenderedObject();
        RenderUtils.invalidateViewState();

        request.setAttribute("protocolBean", protocolBean);

        if (request.getParameter("back") != null) {
            return forward("/protocols/prepareEditProtocolData.jsp");
        }

        if (!protocolBean.internalResponsiblesCorrect()) {
            setMessage(request, "errorMessage", new ActionMessage("error.protocols.invalidInternalResponsibles"));
            return forward("/protocols/prepareEditInternalResponsibles.jsp");
        }

        return forward("/protocols/prepareEditExternalResponsibles.jsp");
    }

    public ActionForward prepareEditProtocolPermissions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolCreationBean protocolBean = getRenderedObject();
        RenderUtils.invalidateViewState();

        request.setAttribute("protocolBean", protocolBean);

        if (request.getParameter("back") != null) {
            return forward("/protocols/prepareEditInternalResponsibles.jsp");
        }

        if (!protocolBean.externalResponsiblesCorrect()) {
            setMessage(request, "errorMessage", new ActionMessage("error.protocols.invalidExternalResponsibles"));
            return forward("/protocols/prepareEditExternalResponsibles.jsp");
        }

        return forward("/protocols/prepareEditProtocolPermissions.jsp");
    }

    public ActionForward editProtocol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolCreationBean protocolBean = getRenderedObject();
        RenderUtils.invalidateViewState();

        request.setAttribute("protocolBean", protocolBean);

        if (request.getParameter("back") != null) {
            return forward("/protocols/prepareEditExternalResponsibles.jsp");
        }

        if (!protocolBean.permissionsCorrectlyDefined()) {
            setMessage(request, "errorMessage", new ActionMessage("error.protocols.invalidProtocolPermissions"));
            return forward("/protocols/prepareEditProtocolPermissions.jsp");
        }

        try {
            protocolBean.getProtocol().updateFromBean(protocolBean);

            setMessage(request, "success", new ActionMessage("label.protocols.successEdit"));

            return viewProtocolDetailsFromOID(request, protocolBean.getProtocol().getExternalId());
        } catch (DomainException e) {
            setMessage(request, "errorMessage", new ActionMessage("error.protocol.number.alreadyExists"));
            return forward("/protocols/prepareEditProtocolPermissions.jsp");
        }
    }

    public ActionForward updateEditionBean(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        ProtocolCreationBean protocolBean = getRenderedObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("protocolBean", protocolBean);

        boolean internal = beanUpdateLogic(request, protocolBean);

        return forward(internal ? "/protocols/prepareEditInternalResponsibles.jsp" : "/protocols/prepareEditExternalResponsibles.jsp");
    }

    /*
     * Protocol Creation
     */

    public ActionForward prepareCreateProtocolData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("protocolBean", new ProtocolCreationBean());

        return forward("/protocols/prepareCreateProtocolData.jsp");
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
                return forward("/protocols/prepareCreateInternalResponsibles.jsp");
            } else {
                return forward("/protocols/prepareCreateProtocolData.jsp");
            }
        } else {
            validateDates(protocolBean.getBeginDate(), protocolBean.getEndDate(), request);
            setMessage(request, "errorMessage", new ActionMessage("error.protocol.number.alreadyExists"));
            return forward("/protocols/prepareCreateProtocolData.jsp");
        }
    }

    // Internal data inserted, proceed to permission setting
    public ActionForward prepareCreateExternalResponsibles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolCreationBean protocolBean = getRenderedObject();
        RenderUtils.invalidateViewState();

        request.setAttribute("protocolBean", protocolBean);

        if (request.getParameter("back") != null) {
            return forward("/protocols/prepareCreateProtocolData.jsp");
        }

        if (!protocolBean.internalResponsiblesCorrect()) {
            setMessage(request, "errorMessage", new ActionMessage("error.protocols.invalidInternalResponsibles"));
            return forward("/protocols/prepareCreateInternalResponsibles.jsp");
        }

        return forward("/protocols/prepareCreateExternalResponsibles.jsp");
    }

    // External data inserted, proceed to permission setting
    public ActionForward prepareDefineProtocolPermissions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolCreationBean protocolBean = getRenderedObject();
        RenderUtils.invalidateViewState();

        request.setAttribute("protocolBean", protocolBean);

        if (request.getParameter("back") != null) {
            return forward("/protocols/prepareCreateInternalResponsibles.jsp");
        }

        if (!protocolBean.externalResponsiblesCorrect()) {
            setMessage(request, "errorMessage", new ActionMessage("error.protocols.invalidExternalResponsibles"));
            return forward("/protocols/prepareCreateExternalResponsibles.jsp");
        }

        return forward("/protocols/prepareDefineProtocolPermissions.jsp");
    }

    // All data inserted, create protocol
    public ActionForward createProtocol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolCreationBean protocolBean = getRenderedObject();
        RenderUtils.invalidateViewState();

        request.setAttribute("protocolBean", protocolBean);

        if (request.getParameter("back") != null) {
            return forward("/protocols/prepareCreateExternalResponsibles.jsp");
        }

        if (!protocolBean.permissionsCorrectlyDefined()) {
            setMessage(request, "errorMessage", new ActionMessage("error.protocols.invalidProtocolPermissions"));
            return forward("/protocols/prepareDefineProtocolPermissions.jsp");
        }

        try {
            Protocol protocol = Protocol.createProtocol(protocolBean);

            setMessage(request, "success", new ActionMessage("label.protocols.successCreate"));

            return viewProtocolDetailsFromOID(request, protocol.getExternalId());
        } catch (DomainException e) {
            setMessage(request, "errorMessage", new ActionMessage("error.protocol.number.alreadyExists"));
            return forward("/protocols/prepareDefineProtocolPermissions.jsp");
        }
    }

    public ActionForward updateBean(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        ProtocolCreationBean protocolBean = getRenderedObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("protocolBean", protocolBean);

        boolean internal = beanUpdateLogic(request, protocolBean);

        return forward(internal ? "/protocols/prepareCreateInternalResponsibles.jsp" : "/protocols/prepareCreateExternalResponsibles.jsp");
    }

    /*
     * Helper methods
     */

    // Terrible hack
    private boolean beanUpdateLogic(final HttpServletRequest request, ProtocolCreationBean protocolBean) {

        boolean internal = true;

        Unit unit = null;

        if (request.getParameter("unitOID") != null) {
            unit = getDomainObject(request, "unitOID");
        }

        if (request.getParameter("insertInternalUnit") != null && protocolBean.getNewUnit() != null) {
            protocolBean.addInternalResponsible(new ProtocolResponsibleBean(protocolBean.getNewUnit()));
            protocolBean.setNewUnit(null);
        } else if (request.getParameter("insertPersonInUnit") != null && protocolBean.getNewPerson() != null) {
            protocolBean.getBeanForUnit(unit).addResponsible(protocolBean.getNewPerson());
            protocolBean.setNewPerson(null);
        } else if (request.getParameter("removePersonInUnit") != null) {
            Person person = getDomainObject(request, "personOID");
            protocolBean.getBeanForUnit(unit).removeResponsible(person);
            protocolBean.setNewPerson(null);
        } else if (request.getParameter("removePersonInExternalUnit") != null) {
            Person person = getDomainObject(request, "personOID");
            protocolBean.getBeanForUnit(unit).removeResponsible(person);
            protocolBean.setNewPerson(null);
            internal = false;
        } else if (request.getParameter("removeUnit") != null) {
            protocolBean.removeUnit(unit);
        } else if (request.getParameter("removeExternalUnit") != null) {
            protocolBean.removeUnit(unit);
            internal = false;
        } else if (request.getParameter("insertExternalUnit") != null) {
            protocolBean.addExternalResponsible(new ProtocolResponsibleBean(protocolBean.getNewUnit()));
            protocolBean.setNewUnit(null);
            internal = false;
        } else if (request.getParameter("insertPersonInExternalUnit") != null && protocolBean.getNewPerson() != null) {
            protocolBean.getBeanForUnit(unit).addResponsible(protocolBean.getNewPerson());
            protocolBean.setNewPerson(null);
            internal = false;
        } else if (request.getParameter("removePosition") != null) {
            protocolBean.getBeanForUnit(unit).removePosition(request.getParameter("position"));
        } else if (request.getParameter("removePositionExternal") != null) {
            protocolBean.getBeanForUnit(unit).removePosition(request.getParameter("position"));
            internal = false;
        } else if (request.getParameter("removePositions") != null) {
            protocolBean.getBeanForUnit(unit).removePositions();
        } else if (request.getParameter("removePositionsExternal") != null) {
            protocolBean.getBeanForUnit(unit).removePositions();
            internal = false;
        } else if (request.getParameter("insertPosition") != null && protocolBean.getNewPosition() != null
                && !protocolBean.getNewPosition().trim().isEmpty()) {
            protocolBean.getBeanForUnit(unit).addPosition(protocolBean.getNewPosition());
            protocolBean.setNewPosition(null);
        } else if (request.getParameter("insertExternalPosition") != null && protocolBean.getNewPosition() != null
                && !protocolBean.getNewPosition().trim().isEmpty()) {
            protocolBean.getBeanForUnit(unit).addPosition(protocolBean.getNewPosition());
            protocolBean.setNewPosition(null);
            internal = false;
        }
        return internal;
    }

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
