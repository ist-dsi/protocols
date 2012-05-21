package module.protocols.domain;

import module.organization.domain.Person;
import module.protocols.domain.util.ProtocolResponsibleType;
import module.protocols.dto.ProtocolCreationBean.ProtocolResponsibleBean;

/**
 * 
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class ProtocolResponsible extends ProtocolResponsible_Base {

    private ProtocolResponsible() {
	super();
    }

    public ProtocolResponsible(ProtocolResponsibleType type) {
	this();
	setType(type);
    }

    /**
     * @param bean
     */
    public void updateFromBean(ProtocolResponsibleBean bean) {
	this.setUnit(bean.getUnit());
	for (Person person : getPeople())
	    removePeople(person);

	for (Person person : bean.getResponsibles())
	    addPeople(person);
    }

}
