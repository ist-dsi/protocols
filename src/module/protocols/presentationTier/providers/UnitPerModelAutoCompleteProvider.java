/**
 * 
 */
package module.protocols.presentationTier.providers;

import java.util.Map;
import java.util.Set;

import module.organization.domain.Party;
import module.organization.presentationTier.renderers.providers.UnitAutoCompleteProvider;
import module.protocols.domain.ProtocolManager;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class UnitPerModelAutoCompleteProvider extends UnitAutoCompleteProvider {

    @Override
    protected Set<Party> getParties(Map<String, String> argsMap, String value) {

	try {

	    String model = argsMap.get("model");

	    if (model.equals("internal")) {

		return ProtocolManager.getInstance().getInternalOrganizationalModel().getPartiesSet();

	    } else if (model.equals("external")) {

		return ProtocolManager.getInstance().getExternalOrganizationalModel().getPartiesSet();

	    } else {
		return super.getParties(argsMap, value);
	    }
	} catch (Exception e) {
	    return super.getParties(argsMap, value);

	}

    }
}
