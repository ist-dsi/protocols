package module.protocols.domain;

import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import module.geography.domain.Country;
import module.geography.domain.GeographicConstants;
import module.geography.domain.GeographicLocation;
import module.organization.domain.AccountabilityType;
import module.organization.domain.Person;
import module.organization.domain.Unit;
import module.protocols.domain.util.ProtocolResponsibleType;
import module.protocols.dto.ProtocolCreationBean.ProtocolResponsibleBean;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.Strings;

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

    public void updateFromBean(ProtocolResponsibleBean bean) {

        this.setUnit(bean.getUnit());

        reloadCountry();

        for (Person person : getPeopleSet()) {
            removePeople(person);
        }

        for (Person person : bean.getResponsibles()) {
            addPeople(person);
        }

        this.setPositionList(bean.getPositions());
    }

    public String getPresentationString() {

        Collection<String> strings = Collections2.transform(getPeopleSet(), new Function<Person, String>() {

            @Override
            public String apply(Person person) {
                return person.getName();
            }

        });

        return getPositionList() == null ? new Strings(strings)
                .getPresentationString() : new Strings(getPositionList(), strings.toArray(new String[0])).getPresentationString();

    }

    public String getCountryDescription() {
        Country country = getCountry();
        if (country == null) {
            return "";
        } else {
            return country.getName().getContent();
        }
    }

    @Atomic
    public void reloadCountry() {
        final AccountabilityType type = AccountabilityType.readBy(GeographicConstants.GEOGRAPHIC_ACCOUNTABILITY_TYPE_NAME);
        final Unit countryUnit = getUnit().getChildAccountabilityStream().filter(a -> a.getAccountabilityType() == type
                && a.isActiveNow())
                .map(a -> a.getChild()).filter(p -> p.isUnit()).map(p -> (Unit) p).findAny().orElse(null);;

        GeographicLocation location = countryUnit != null ? countryUnit.getGeographicLocation() : Country.getPortugal();

        setCountry((location instanceof Country) ? (Country) location : null);
    }

}
