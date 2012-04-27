/**
 * 
 */
package module.protocols.presentationTier.providers;

import java.util.Collection;
import java.util.Map;

import module.organization.domain.Person;
import module.organization.domain.Unit;
import module.organization.presentationTier.renderers.providers.PersonAutoCompleteProvider;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class PeoplePerUnitAutoCompletProvider extends PersonAutoCompleteProvider {

    @Override
    protected Collection<Person> getPeople(Map<String, String> argsMap, String value) {

	try {

	    String unitOID = argsMap.get("unit");

	    Unit unit = Unit.fromExternalId(unitOID);

	    return unit.getChildPersons();

	} catch (Exception e) {
	    return super.getPeople(argsMap, value);

	}
    }

}
