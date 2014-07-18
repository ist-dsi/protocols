package module.protocols.domain;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;

public class ProtocolsDomainException extends DomainException {

    private static final long serialVersionUID = -7278422683996857940L;

    protected ProtocolsDomainException(String key, String... args) {
        super("resources.ProtocolsResources", key, args);
    }

    public static DomainException groupHasProtocols() {
        return new ProtocolsDomainException("error.group.has.protocols");
    }

    public static DomainException unauthorized() {
        return new ProtocolsDomainException("error.unauthorized");
    }

}
