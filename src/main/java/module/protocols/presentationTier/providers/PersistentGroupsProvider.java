/**
 * 
 */
package module.protocols.presentationTier.providers;

import module.protocols.domain.ProtocolAdministrativeGroup;
import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.groups.IntersectionGroup;
import pt.ist.bennu.core.domain.groups.NegationGroup;
import pt.ist.bennu.core.domain.groups.PersistentGroup;
import pt.ist.bennu.core.domain.groups.SingleUserGroup;
import pt.ist.bennu.core.domain.groups.UnionGroup;
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
                /*
                 * Small hacks so the interface does not get so sluggish.
                 */
                if (group instanceof ProtocolAdministrativeGroup || group instanceof UnionGroup
                        || group instanceof IntersectionGroup || group instanceof SingleUserGroup
                        || group instanceof NegationGroup) {
                    return false;
                }

                String className = group.getClass().getName();

                if (className.startsWith("module.workflow") || className.startsWith("module.siadap")) {
                    return false;
                }
                return true;
            }
        });
    }

}
