package module.protocols.domain;

import java.util.Collection;

import module.geography.domain.Country;
import module.geography.domain.GeographicConstants;
import module.geography.domain.GeographicLocation;
import module.organization.domain.AccountabilityType;
import module.organization.domain.Party;
import module.organization.domain.Person;
import module.organization.domain.Unit;
import module.protocols.domain.util.ProtocolResponsibleType;
import module.protocols.dto.ProtocolCreationBean.ProtocolResponsibleBean;
import pt.ist.fenixWebFramework.services.Service;
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

		reloadCountry();

		for (Person person : getPeople()) {
			removePeople(person);
		}

		for (Person person : bean.getResponsibles()) {
			addPeople(person);
		}

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

	public String getCountryDescription() {
		Country country = getCountry();
		if (country == null) {
			return "";
		} else {
			return country.getName().getContent();
		}
	}

	@Service
	public void reloadCountry() {

		Collection<Party> geoChildren =
				getUnit().getChildren(AccountabilityType.readBy(GeographicConstants.GEOGRAPHIC_ACCOUNTABILITY_TYPE_NAME));

		Unit countryUnit = null;

		for (Party party : geoChildren) {
			if (party.isUnit()) {
				countryUnit = (Unit) party;
				break;
			}
		}

		if (countryUnit != null) {

			GeographicLocation location = countryUnit.getGeographicLocation();

			setCountry((location instanceof Country) ? (Country) location : null);
		}

	}
}
