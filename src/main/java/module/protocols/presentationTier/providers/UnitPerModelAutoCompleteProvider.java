/**
 * 
 */
package module.protocols.presentationTier.providers;

import java.util.Map;
import java.util.Set;

import module.organization.domain.OrganizationalModel;
import module.organization.domain.Party;
import module.organization.domain.Unit;
import module.organization.presentationTier.renderers.providers.UnitAutoCompleteProvider;
import module.protocols.domain.ProtocolManager;

import org.fenixedu.bennu.core.domain.Bennu;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class UnitPerModelAutoCompleteProvider extends UnitAutoCompleteProvider {

    @Override
    protected Set<? extends Party> getParties(Map<String, String> argsMap, String value) {

        try {
            String model = argsMap.get("model");

            if (model.equals("internal")) {
                return getUnitsFromModel(ProtocolManager.getInstance().getInternalOrganizationalModel());
            } else if (model.equals("external")) {
                // External model only has top-level units, do a direct search
                return ProtocolManager.getInstance().getExternalOrganizationalModel().getPartiesSet();
            } else {
                return super.getParties(argsMap, value);
            }

        } catch (Exception e) {
            return super.getParties(argsMap, value);
        }

    }

    private Set<Party> getUnitsFromModel(final OrganizationalModel model) {

        if (model == null) {
            throw new RuntimeException();
        }

        return Sets.filter(Bennu.getInstance().getPartiesSet(), new Predicate<Party>() {

            @Override
            public boolean apply(Party par) {

                if (!(par instanceof Unit)) {
                    return false;
                }

                return model.containsUnit(par);
            }

        });
    }
}
