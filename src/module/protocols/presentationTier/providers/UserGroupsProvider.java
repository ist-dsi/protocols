/**
 * 
 */
package module.protocols.presentationTier.providers;

import module.protocols.domain.ProtocolAuthorizationGroup;
import module.protocols.domain.ProtocolManager;
import myorg.applicationTier.Authenticate;
import myorg.domain.User;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.utl.ist.fenix.tools.util.CollectionUtils;
import pt.utl.ist.fenix.tools.util.Predicate;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class UserGroupsProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object arg0, Object arg1) {

	final User user = Authenticate.getCurrentUser();

	return CollectionUtils.filter(ProtocolManager.getInstance().getProtocolAuthorizationGroups(),
		new Predicate<ProtocolAuthorizationGroup>() {

		    @Override
		    public boolean evaluate(ProtocolAuthorizationGroup group) {

			return group.getAuthorizedWriterGroup().isMember(user);
		    }
		});
    }
}
