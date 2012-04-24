/**
 * 
 */
package module.protocols.presentationTier.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import module.organization.domain.Party;
import module.organization.domain.Unit;
import module.protocols.domain.ProtocolManager;
import myorg.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class PartnerNameProvider implements AutoCompleteProvider {

    @Override
    public Collection<?> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
	final List<Unit> partners = new ArrayList<Unit>();

	String values = StringNormalizer.normalize(value.trim()).toLowerCase();

	for (final Unit unit : ProtocolManager.getInstance().getExternalOrganizationalModel().getAllUnits()) {
	    final String normalizedName = StringNormalizer.normalize(unit.getPresentationName()).toLowerCase();
	    if (StringUtils.containsAny(values, normalizedName)) {
		partners.add(unit);
	    }

	    if (partners.size() >= maxCount) {
		break;
	    }
	}

	Collections.sort(partners, Party.COMPARATOR_BY_NAME);

	return partners;
    }
}
