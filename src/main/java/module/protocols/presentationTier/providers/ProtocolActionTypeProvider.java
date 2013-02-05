package module.protocols.presentationTier.providers;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 *
 */

import java.util.Arrays;

import module.protocols.domain.util.ProtocolActionType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumArrayConverter;

public class ProtocolActionTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return Arrays.asList(ProtocolActionType.values());
    }

    @Override
    public Converter getConverter() {
        return new EnumArrayConverter(ProtocolActionType.class);
    }

}
