/**
 * 
 */
package module.protocols.presentationTier.providers;

import module.protocols.dto.ProtocolCreationBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class AuthorizationGroupReadersProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object source, Object currentValue) {

	ProtocolCreationBean bean = (ProtocolCreationBean) source;

	return bean.getWriters().getAuthorizedReaderGroups();
    }

}
