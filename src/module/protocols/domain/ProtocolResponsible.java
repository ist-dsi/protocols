package module.protocols.domain;

import module.organization.domain.Unit;
import module.protocols.domain.util.ProtocolResponsibleType;

/**
 * 
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class ProtocolResponsible extends ProtocolResponsible_Base {

    private ProtocolResponsible() {
	super();
    }

    public ProtocolResponsible(ProtocolResponsibleType type, Unit unit) {
	this();
	setType(type);
	setUnit(unit);
    }

}
