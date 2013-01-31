package module.protocols.dto;

import java.io.Serializable;
import java.util.List;

import module.geography.domain.Country;
import module.organization.domain.Unit;
import module.protocols.domain.Protocol;
import module.protocols.domain.ProtocolHistory;
import module.protocols.domain.ProtocolResponsible;
import module.protocols.domain.util.ProtocolActionType;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import pt.ist.bennu.core.applicationTier.Authenticate;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.bennu.core.util.IntervalTools;
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

import com.google.common.base.Predicate;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class ProtocolSearchBean implements Serializable, Predicate<Protocol> {

	private static final long serialVersionUID = 2466817555993209410L;

	public static enum SearchNationalityType implements IPresentableEnum {
		NATIONAL, INTERNATIONAL, WITHOUT_NATIONALITY, COUNTRY;

		@Override
		public String getLocalizedName() {
			return BundleUtil
					.getStringFromResourceBundle("resources/ProtocolsResources", "label.searchNationalityType." + name());
		}
	}

	/*
	 * Boring bean stuff
	 */

	private String protocolNumber;

	private Integer year;

	private LocalDate beginProtocolBeginDate;

	private LocalDate endProtocolBeginDate;

	private LocalDate beginProtocolEndDate;

	private LocalDate endProtocolEndDate;

	private LocalDate beginSignedDate;

	private LocalDate endSignedDate;

	private List<ProtocolActionType> protocolActionTypes;

	private String otherProtocolActionTypes;

	private Unit partner;

	private boolean actives;

	private boolean inactives;

	private boolean national = true;

	private boolean international;

	private Country country;

	private SearchNationalityType nationalityType;

	public ProtocolSearchBean() {
		super();
		setActives(true);
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public LocalDate getBeginProtocolBeginDate() {
		return beginProtocolBeginDate;
	}

	public void setBeginProtocolBeginDate(LocalDate beginProtocolBeginDate) {
		this.beginProtocolBeginDate = beginProtocolBeginDate;
	}

	public LocalDate getEndProtocolBeginDate() {
		return endProtocolBeginDate;
	}

	public void setEndProtocolBeginDate(LocalDate endProtocolBeginDate) {
		this.endProtocolBeginDate = endProtocolBeginDate;
	}

	public LocalDate getBeginProtocolEndDate() {
		return beginProtocolEndDate;
	}

	public void setBeginProtocolEndDate(LocalDate beginProtocolEndDate) {
		this.beginProtocolEndDate = beginProtocolEndDate;
	}

	public LocalDate getEndProtocolEndDate() {
		return endProtocolEndDate;
	}

	public void setEndProtocolEndDate(LocalDate endProtocolEndDate) {
		this.endProtocolEndDate = endProtocolEndDate;
	}

	public LocalDate getBeginSignedDate() {
		return beginSignedDate;
	}

	public void setBeginSignedDate(LocalDate beginSignedDate) {
		this.beginSignedDate = beginSignedDate;
	}

	public LocalDate getEndSignedDate() {
		return endSignedDate;
	}

	public void setEndSignedDate(LocalDate endSignedDate) {
		this.endSignedDate = endSignedDate;
	}

	public List<ProtocolActionType> getProtocolActionTypes() {
		return protocolActionTypes;
	}

	public void setProtocolActionTypes(List<ProtocolActionType> protocolActionTypes) {
		this.protocolActionTypes = protocolActionTypes;
	}

	public String getOtherProtocolActionTypes() {
		return otherProtocolActionTypes;
	}

	public void setOtherProtocolActionTypes(String otherProtocolActionTypes) {
		this.otherProtocolActionTypes = otherProtocolActionTypes;
	}

	public Unit getPartner() {
		return partner;
	}

	public void setPartner(Unit partner) {
		this.partner = partner;
	}

	public boolean isActives() {
		return actives;
	}

	public void setActives(boolean actives) {
		this.actives = actives;
	}

	public boolean isInactives() {
		return inactives;
	}

	public void setInactives(boolean inactives) {
		this.inactives = inactives;
	}

	public boolean isNational() {
		return national;
	}

	public void setNational(boolean national) {
		this.national = national;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public SearchNationalityType getNationalityType() {
		return nationalityType;
	}

	public void setNationalityType(SearchNationalityType nationalityType) {
		this.nationalityType = nationalityType;
	}

	public boolean isInternational() {
		return international;
	}

	public void setInternational(boolean international) {
		this.international = international;
	}

	/*
	 * Fun stuff
	 */

	private boolean satisfiesAnyProtocolHistoryDate(LocalDate beginProtocolBeginDate, LocalDate endProtocolBeginDate,
			Protocol protocol) {
		for (ProtocolHistory protocolHistory : protocol.getProtocolHistories()) {
			if (satisfiedDates(getBeginProtocolBeginDate(), getEndProtocolBeginDate(), protocolHistory.getBeginDate())
					&& satisfiedDates(getBeginProtocolEndDate(), getEndProtocolEndDate(), protocolHistory.getBeginDate())) {
				return true;
			}
		}
		return false;
	}

	private boolean satisfiedActiveInYear(Protocol protocol) {
		if (getYear() != null) {
			for (ProtocolHistory protocolHistory : protocol.getProtocolHistories()) {
				if (protocolHistory.getEndDate() == null) {
					return protocolHistory.getBeginDate() == null || protocolHistory.getBeginDate().getYear() <= getYear();
				} else {
					return (protocolHistory.getBeginDate() == null || protocolHistory.getBeginDate().getYear() <= getYear())
							&& protocolHistory.getEndDate().getYear() >= getYear();
				}
			}
		}
		return true;
	}

	private boolean satisfiedDates(LocalDate beginDate, LocalDate endDate, LocalDate date) {
		if (beginDate != null && date != null) {
			if (endDate != null) {
				Interval interval = IntervalTools.getInterval(beginDate, endDate);
				return interval.contains(date.toDateMidnight());
			} else {
				return !beginDate.isAfter(date);
			}
		}
		return true;
	}

	public boolean satisfiedActivity(Protocol protocol) {
		if ((protocol.isActive() && isActives()) || (isInactives() && !protocol.isActive())) {
			return true;
		}
		return false;
	}

	private boolean satisfiedNationality(Protocol protocol) {

		if (nationalityType == null) {
			return true;
		}

		switch (nationalityType) {
		case NATIONAL:
			return protocol.isNational();
		case INTERNATIONAL:
			return protocol.isInternational();
		case WITHOUT_NATIONALITY:
			return !protocol.hasNationality();
		case COUNTRY:
			return protocol.hasPartnerInCountry(country);
		}

		return false;
	}

	private boolean satiefiedProtocolActionTypes(Protocol protocol) {
		return (getProtocolActionTypes() == null || protocol.getProtocolAction().contains(getProtocolActionTypes()));
	}

	private boolean satisfiedProtocolPartner(Protocol protocol) {

		if (getPartner() != null) {
			for (ProtocolResponsible responsible : protocol.getProtocolResponsible()) {
				if (responsible.getUnit().equals(getPartner())) {
					return true;
				}
			}
			return false;
		}

		return true;
	}

	private boolean satisfiedOtherProtocolActionTypes(Protocol protocol) {
		return StringUtils.isEmpty(getOtherProtocolActionTypes())
				|| StringUtils.containsAny(protocol.getProtocolAction().getOtherTypes(), getOtherProtocolActionTypes());
	}

	private boolean satisfiedProtocolNumber(Protocol protocol) {
		return (StringUtils.isEmpty(getProtocolNumber()) || StringNormalizer.normalize(protocol.getProtocolNumber()).indexOf(
				StringNormalizer.normalize(getProtocolNumber())) != -1);
	}

	@Override
	public boolean apply(Protocol protocol) {

		// System.out.println("Number: " + satisfiedProtocolNumber(protocol));
		// System.out.println("History dates: "
		// + satisfiesAnyProtocolHistoryDate(getBeginProtocolBeginDate(),
		// getEndProtocolBeginDate(), protocol));
		// System.out.println("Signed: " + satisfiedDates(getBeginSignedDate(),
		// getEndSignedDate(), protocol.getSignedDate()));
		// System.out.println("Action types: "
		// + (satisfiedOtherProtocolActionTypes(protocol) &&
		// satiefiedProtocolActionTypes(protocol)));
		// System.out.println("Partner: " + satisfiedProtocolPartner(protocol));
		// System.out.println("Nacionalidade: " +
		// satisfiedNationality(protocol));
		// System.out.println("Actividade: " + satisfiedActivity(protocol));
		// System.out.println("Activo no ano: " +
		// satisfiedActiveInYear(protocol));
		// System.out.println("Visivel: " +
		// protocol.canBeReadByUser(Authenticate.getCurrentUser()));

		// System.out.println("valores: " +
		// ReflectionToStringBuilder.toString(this,
		// ToStringStyle.MULTI_LINE_STYLE));

		return satisfiedProtocolNumber(protocol)
				&& satisfiesAnyProtocolHistoryDate(getBeginProtocolBeginDate(), getEndProtocolBeginDate(), protocol)
				&& satisfiedDates(getBeginSignedDate(), getEndSignedDate(), protocol.getSignedDate())
				&& satisfiedOtherProtocolActionTypes(protocol) && satiefiedProtocolActionTypes(protocol)
				&& satisfiedProtocolPartner(protocol) && satisfiedNationality(protocol) && satisfiedActivity(protocol)
				&& satisfiedActiveInYear(protocol) && protocol.canBeReadByUser(Authenticate.getCurrentUser());
	}

}
