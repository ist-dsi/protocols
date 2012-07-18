/**
 * 
 */
package module.protocols.presentationTier.providers;

import java.util.ArrayList;

import module.organization.domain.OrganizationalModel;
import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class OrganizationalModelProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	return new ArrayList<OrganizationalModel>(MyOrg.getInstance().getOrganizationalModels());
    }

}
