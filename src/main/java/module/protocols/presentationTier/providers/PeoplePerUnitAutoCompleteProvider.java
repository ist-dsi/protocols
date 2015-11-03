/**
 * 
 */
package module.protocols.presentationTier.providers;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import module.organization.domain.Person;
import module.organization.domain.Unit;
import module.organization.presentationTier.renderers.providers.PersonAutoCompleteProvider;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class PeoplePerUnitAutoCompleteProvider extends PersonAutoCompleteProvider {

    @Override
    protected Collection<Person> getPersons(Map<String, String> argsMap, String value) {

        try {

            String unitOID = argsMap.get("unit");

            Unit unit = FenixFramework.getDomainObject(unitOID);

            Collection<Person> people = unit.getChildAccountabilityStream().map(a -> a.getChild()).filter(p -> p.isPerson())
                    .map(p -> (Person) p).collect(Collectors.toList());

            if (people.size() == 0) {
                return super.getPersons(argsMap, value);
            } else {
                return people;
            }

        } catch (Exception e) {
            return super.getPersons(argsMap, value);
        }
    }

}
