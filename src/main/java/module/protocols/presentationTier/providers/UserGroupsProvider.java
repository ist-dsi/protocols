/**
 * 
 */
package module.protocols.presentationTier.providers;

import java.util.stream.Collectors;

import module.protocols.domain.ProtocolAuthorizationGroup;
import module.protocols.domain.ProtocolManager;

import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

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
        if (ProtocolManager.managers().isMember(Authenticate.getUser())) {
            return ProtocolManager.getInstance().getProtocolAuthorizationGroupsSet();
        }

        return ProtocolManager.getInstance().getProtocolAuthorizationGroupsSet().stream()
                .filter(ProtocolAuthorizationGroup::isAccessible).collect(Collectors.toList());
    }
}
