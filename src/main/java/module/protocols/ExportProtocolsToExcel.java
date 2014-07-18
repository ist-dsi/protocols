package module.protocols;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import module.protocols.domain.Protocol;
import module.protocols.domain.ProtocolHistory;
import module.protocols.domain.ProtocolManager;
import module.protocols.domain.ProtocolResponsible;
import module.protocols.domain.util.ProtocolActionType;
import module.protocols.domain.util.ProtocolResponsibleType;
import pt.ist.bennu.core.domain.scheduler.WriteCustomTask;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

public class ExportProtocolsToExcel extends WriteCustomTask {

    @Override
    protected void doService() {
        Spreadsheet spreadsheet = new Spreadsheet("Protocolos");

        spreadsheet.setHeader("Número");
        spreadsheet.setHeader("Responsáveis Internos");
        spreadsheet.setHeader("Responsáveis Externos");
        spreadsheet.setHeader("Data de assinatura");
        spreadsheet.setHeader("Datas vigentes");
        spreadsheet.setHeader("Áreas Científicas");
        spreadsheet.setHeader("Tipos de Acção");
        spreadsheet.setHeader("Outros");
        spreadsheet.setHeader("Observações");

        List<Protocol> protocols = new ArrayList<Protocol>(ProtocolManager.getInstance().getProtocolsSet());

        Collections.sort(protocols, new Comparator<Protocol>() {
            @Override
            public int compare(Protocol o1, Protocol o2) {
                return o1.getProtocolNumber().compareTo(o2.getProtocolNumber());
            }
        });

        for (Protocol protocol : protocols) {
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
        }

        try {
            spreadsheet.exportToXLSSheet(new File("/Users/jpc/Desktop/protocolos.xls"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //storeFileOutput("protocolos.xls", "protocolos.xls", spreadsheet);
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
