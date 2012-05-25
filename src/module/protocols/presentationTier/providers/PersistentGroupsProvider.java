/**
 * 
 */
package module.protocols.presentationTier.providers;

import module.protocols.domain.ProtocolAdministrativeGroup;
import myorg.domain.MyOrg;
import myorg.domain.groups.PersistentGroup;
import myorg.domain.groups.UnionGroup;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class PersistentGroupsProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object arg0, Object arg1) {
	return Collections2.filter(MyOrg.getInstance().getPersistentGroups(), new Predicate<PersistentGroup>() {

	    @Override
	    public boolean apply(PersistentGroup group) {
		if (group instanceof ProtocolAdministrativeGroup)
		    return false;

		if (group instanceof UnionGroup)
		    return false;

		return true;
	    }
	});
    }

}
