package module.protocols.presentationTier.actions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.protocols.domain.Protocol;
import module.protocols.domain.ProtocolHistory;
import module.protocols.domain.ProtocolManager;
import module.protocols.domain.ProtocolResponsible;
import module.protocols.domain.util.ProtocolActionType;
import module.protocols.domain.util.ProtocolResponsibleType;
import module.protocols.dto.ProtocolSearchBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.presentationTier.actions.BaseAction;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

@StrutsFunctionality(app = ProtocolsDispatchAction.class, path = "search", titleKey = "label.protocols.search")
@Mapping(path = "/searchProtocols")
public class SearchProtocolsAction extends BaseAction {

    @EntryPoint
    public ActionForward searchProtocols(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        ProtocolSearchBean bean = getRenderedObject();
        if (bean != null && request.getParameter("search") != null) {
            request.setAttribute("searchResults",
                    ProtocolManager.getInstance().getProtocolsSet().stream().filter(bean).collect(Collectors.toList()));
        } else if (bean == null) {
            bean = new ProtocolSearchBean();
        }

        request.setAttribute("protocolSearch", bean);

        return forward("/protocols/searchProtocols.jsp");
    }

    public ActionForward exportSearchResultsToExcel(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        ProtocolSearchBean bean = getRenderedObject();
        Stream<Protocol> filteredProtocols =
                ProtocolManager.getInstance().getProtocolsSet().stream().filter(bean)
                        .sorted(Comparator.comparing(Protocol::getProtocolNumber));

        Spreadsheet spreadsheet = new Spreadsheet("Protocols");

        spreadsheet.setHeader(BundleUtil.getString("resources.ProtocolsResources", "label.protocols.number"));
        spreadsheet.setHeader(BundleUtil.getString("resources.ProtocolsResources", "label.protocols.internalResponsibles"));
        spreadsheet.setHeader(BundleUtil.getString("resources.ProtocolsResources", "label.protocols.externalResponsibles"));
        spreadsheet.setHeader(BundleUtil.getString("resources.ProtocolsResources", "label.protocols.signedDate"));
        spreadsheet.setHeader(BundleUtil.getString("resources.ProtocolsResources", "label.protocols.actualDates"));
        spreadsheet.setHeader(BundleUtil.getString("resources.ProtocolsResources", "label.protocols.scientificAreas"));
        spreadsheet.setHeader(BundleUtil.getString("resources.ProtocolsResources", "label.protocol.actionTypes"));
        spreadsheet.setHeader(BundleUtil.getString("resources.ProtocolsResources", "label.protocol.otherActionTypes"));
        spreadsheet.setHeader(BundleUtil.getString("resources.ProtocolsResources", "label.protocols.observations"));

        filteredProtocols.forEach((protocol) -> {
            Row row = spreadsheet.addRow();
            row.setCell(protocol.getProtocolNumber());
            row.setCell(Joiner.on('\n').join(responsibles(protocol, true)));
            row.setCell(Joiner.on('\n').join(responsibles(protocol, false)));
            row.setCell(protocol.getSignedDate() == null ? "-" : protocol.getSignedDate().toString("YYYY/MM/dd"));
            row.setCell(Joiner.on('\n').join(
                    FluentIterable.from(protocol.getCurrentAndFutureProtocolHistories()).transform(
                            new Function<ProtocolHistory, String>() {
                                @Override
                                public String apply(ProtocolHistory input) {
                                    return (input.getBeginDate() == null ? "" : input.getBeginDate().toString("YYYY/MM/dd"))
                                            + " - "
                                            + (input.getEndDate() == null ? "" : input.getEndDate().toString("YYYY/MM/dd"));
                                }
                            })));
            row.setCell(protocol.getScientificAreas());
            row.setCell(Joiner.on(" , ").join(
                    FluentIterable.from(protocol.getProtocolAction().getProtocolActionTypes()).transform(
                            new Function<ProtocolActionType, String>() {
                                @Override
                                public String apply(ProtocolActionType input) {
                                    return input.getLocalizedName();
                                }
                            })));
            row.setCell(protocol.getProtocolAction().getOtherTypes());
            row.setCell(protocol.getObservations());
        });

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        spreadsheet.exportToXLSSheet(outputStream);
        download(response, "protocols.xls", outputStream.toByteArray(), "application/vnd.ms-excel");

        return null;
    }

    private Iterable<String> responsibles(Protocol protocol, final boolean internal) {
        return FluentIterable.from(protocol.getProtocolResponsibleSet()).filter(new Predicate<ProtocolResponsible>() {
            @Override
            public boolean apply(ProtocolResponsible resp) {
                return (resp.getType() == ProtocolResponsibleType.INTERNAL) == internal;
            }
        }).transform(new Function<ProtocolResponsible, String>() {
            @Override
            public String apply(ProtocolResponsible resp) {
                return resp.getUnit().getPresentationName() + " - " + resp.getPresentationString();
            }
        });
    }

}
