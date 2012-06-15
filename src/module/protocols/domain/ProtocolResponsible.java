package module.protocols.domain;

import java.util.Collection;

import module.organization.domain.Person;
import module.protocols.domain.util.ProtocolResponsibleType;
import module.protocols.dto.ProtocolCreationBean.ProtocolResponsibleBean;
import pt.utl.ist.fenix.tools.util.Strings;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

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

	this.setPositionList(bean.getPositions());
    }

    public String getPresentationString() {

	Collection<String> strings = Collections2.transform(getPeople(), new Function<Person, String>() {

	    @Override
	    public String apply(Person person) {
		return person.getName();
	    }

	});

	return getPositionList() == null ? new Strings(strings).getPresentationString() : new Strings(getPositionList(),
		strings.toArray(new String[0])).getPresentationString();

    }
}
