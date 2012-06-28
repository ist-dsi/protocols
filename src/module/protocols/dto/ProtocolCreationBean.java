package module.protocols.dto;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 *
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import module.organization.domain.Person;
import module.organization.domain.Unit;
import module.protocols.domain.Protocol;
import module.protocols.domain.ProtocolAuthorizationGroup;
import module.protocols.domain.ProtocolHistory;
import module.protocols.domain.ProtocolManager;
import module.protocols.domain.ProtocolResponsible;
import module.protocols.domain.ProtocolVisibilityType;
import module.protocols.domain.util.ProtocolActionType;
import module.protocols.domain.util.ProtocolResponsibleType;
import myorg.applicationTier.Authenticate;
import myorg.domain.groups.PersistentGroup;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.Strings;

public class ProtocolCreationBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3819698830239113842L;

    public static class ProtocolResponsibleBean implements Serializable, Comparable<ProtocolResponsibleBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1303497805898307379L;

	private final Unit unit;

	private final Set<Person> responsibles;

	private Strings positions;

	private final ProtocolResponsible protocolResponsible;

	public ProtocolResponsibleBean(ProtocolResponsible responsible) {
	    this.protocolResponsible = responsible;
	    this.unit = responsible.getUnit();
	    this.responsibles = new HashSet<Person>(responsible.getPeople());
	    this.positions = responsible.getPositionList() == null ? new Strings(new String[0]) : responsible.getPositionList();
	}

	public ProtocolResponsibleBean(Unit unit) {
	    super();
	    this.unit = unit;
	    this.responsibles = new HashSet<Person>();
	    this.protocolResponsible = null;
	    this.positions = new Strings(new String[0]);
	}

	public Unit getUnit() {
	    return unit;
	}

	public Set<Person> getResponsibles() {
	    return responsibles;
	}

	public void addResponsible(Person responsible) {
	    if (responsible != null && !responsibles.contains(responsible))
		responsibles.add(responsible);
	}

	public Strings getPositions() {
	    return positions;
	}

	public void addPosition(String position) {
	    if (positions != null && !positions.contains(position))
		positions = new Strings(positions, position);
	}

	public boolean check() {
	    return unit != null;
	}

	/**
	 * @param newPerson
	 */
	public void removeResponsible(Person newPerson) {
	    responsibles.remove(newPerson);
	}

	public ProtocolResponsible getProtocolResponsible() {
	    return protocolResponsible;
	}

	@Override
	public int compareTo(ProtocolResponsibleBean o) {
	    return Unit.COMPARATOR_BY_PRESENTATION_NAME.compare(this.unit, o.getUnit());
	}

	/**
	 * 
	 */
	public void removePositions() {
	    positions = new Strings(new String[0]);
	}
    }

    private Protocol protocol;

    /*
     * Step 1
     */

    private String protocolNumber;

    private LocalDate signedDate;

    private LocalDate beginDate;

    private LocalDate endDate;

    private String scientificAreas;

    private List<ProtocolActionType> actionTypes;

    private String otherActionTypes;

    private String observations;

    /*
     * Step 2
     */

    private SortedSet<ProtocolResponsibleBean> internalResponsibles;

    private Unit newUnit;

    private Person newPerson;

    private String newPosition;

    /*
     * Step 3
     */

    private SortedSet<ProtocolResponsibleBean> externalResponsibles;

    /*
     * Step 4
     */

    private ProtocolAuthorizationGroup writers;

    private List<PersistentGroup> readers;

    private ProtocolVisibilityType visibilityType = ProtocolVisibilityType.TOTAL;

    /*
     * Extra stuff
     */

    private List<ProtocolResponsible> removedResponsibles;

    public ProtocolCreationBean() {

    }

    public ProtocolCreationBean(Protocol protocol) {

	// Step 1

	setProtocol(protocol);

	setProtocolNumber(protocol.getProtocolNumber());

	setSignedDate(protocol.getSignedDate());

	ProtocolHistory history = protocol.getCurrentProtocolHistory(); // TODO
									// Check

	if (history != null) {
	    setBeginDate(history.getBeginDate());
	    setEndDate(history.getEndDate());
	}

	setScientificAreas(protocol.getScientificAreas());

	setActionTypes(new ArrayList<ProtocolActionType>(protocol.getProtocolAction().getProtocolActionTypes()));

	setOtherActionTypes(protocol.getProtocolAction().getOtherTypes());

	setObservations(protocol.getObservations());

	// Steps 2 and 3

	for (ProtocolResponsible responsible : protocol.getProtocolResponsible()) {
	    ProtocolResponsibleBean bean = new ProtocolResponsibleBean(responsible);
	    if (responsible.getType() == ProtocolResponsibleType.INTERNAL) {
		addInternalResponsible(bean);
	    } else {
		addExternalResponsible(bean);
	    }
	}

	setWriters(protocol.getWriterGroup());

	setReaders(new ArrayList<PersistentGroup>(protocol.getReaderGroups()));

	setVisibilityType(protocol.getVisibilityType());

    }

    public Protocol getProtocol() {
	return protocol;
    }

    public void setProtocol(Protocol protocol) {
	this.protocol = protocol;
    }

    public String getProtocolNumber() {
	return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
	this.protocolNumber = protocolNumber;
    }

    public LocalDate getSignedDate() {
	return signedDate;
    }

    public void setSignedDate(LocalDate signedDate) {
	this.signedDate = signedDate;
    }

    public LocalDate getBeginDate() {
	return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
	this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
	return endDate;
    }

    public void setEndDate(LocalDate endDate) {
	this.endDate = endDate;
    }

    public String getScientificAreas() {
	return scientificAreas;
    }

    public void setScientificAreas(String scientificAreas) {
	this.scientificAreas = scientificAreas;
    }

    public List<ProtocolActionType> getActionTypes() {
	return actionTypes;
    }

    public void setActionTypes(List<ProtocolActionType> actionTypes) {
	this.actionTypes = actionTypes;
    }

    public String getOtherActionTypes() {
	return otherActionTypes;
    }

    public void setOtherActionTypes(String otherActionTypes) {
	this.otherActionTypes = otherActionTypes;
    }

    public String getObservations() {
	return observations;
    }

    public void setObservations(String observations) {
	this.observations = observations;
    }

    public Set<ProtocolResponsibleBean> getInternalResponsibles() {
	return internalResponsibles;
    }

    public void setInternalResponsibles(SortedSet<ProtocolResponsibleBean> internalResponsibles) {
	this.internalResponsibles = internalResponsibles;
    }

    public SortedSet<ProtocolResponsibleBean> getExternalResponsibles() {
	return externalResponsibles;
    }

    public void setExternalResponsibles(SortedSet<ProtocolResponsibleBean> externalResponsibles) {
	this.externalResponsibles = externalResponsibles;
    }

    public ProtocolAuthorizationGroup getWriters() {
	return writers;
    }

    public void setWriters(ProtocolAuthorizationGroup writers) {
	this.writers = writers;
	this.readers = null;
    }

    public List<PersistentGroup> getReaders() {
	return readers;
    }

    public void setReaders(List<PersistentGroup> readers) {
	this.readers = readers;
    }

    public ProtocolVisibilityType getVisibilityType() {
	return visibilityType;
    }

    public void setVisibilityType(ProtocolVisibilityType visibilityType) {
	this.visibilityType = visibilityType;
    }

    public void addInternalResponsible(ProtocolResponsibleBean bean) {
	if (internalResponsibles == null)
	    internalResponsibles = new TreeSet<ProtocolResponsibleBean>();

	internalResponsibles.add(bean);
    }

    public void addExternalResponsible(ProtocolResponsibleBean bean) {
	if (externalResponsibles == null)
	    externalResponsibles = new TreeSet<ProtocolResponsibleBean>();

	externalResponsibles.add(bean);
    }

    public Unit getNewUnit() {
	return newUnit;
    }

    public void setNewUnit(Unit newUnit) {
	this.newUnit = newUnit;
    }

    public Person getNewPerson() {
	return newPerson;
    }

    public void setNewPerson(Person newPerson) {
	this.newPerson = newPerson;
    }

    public String getNewPosition() {
	return newPosition;
    }

    public void setNewPosition(String newPosition) {
	this.newPosition = newPosition;
    }

    public Person getCreator() {
	return Authenticate.getCurrentUser().getPerson();
    }

    public List<ProtocolResponsible> getRemovedResponsibles() {
	return removedResponsibles;
    }

    public void removeResponsible(ProtocolResponsibleBean bean) {
	if (removedResponsibles == null)
	    removedResponsibles = new ArrayList<ProtocolResponsible>();

	removedResponsibles.add(bean.getProtocolResponsible());
    }

    /**
     * Verifies whether the internal responsibles are correctly set
     * 
     * @return
     */
    public boolean internalResponsiblesCorrect() {

	if (internalResponsibles == null || internalResponsibles.size() == 0)
	    return false;

	for (ProtocolResponsibleBean bean : internalResponsibles) {
	    if (!bean.check())
		return false;
	}

	return true;

    }

    /**
     * Verifies whether the external responsibles are correctly set
     * 
     * @return
     */
    public boolean externalResponsiblesCorrect() {

	if (externalResponsibles == null || externalResponsibles.size() == 0)
	    return false;

	for (ProtocolResponsibleBean bean : externalResponsibles) {
	    if (!bean.check())
		return false;
	}

	return true;
    }

    public boolean permissionsCorrectlyDefined() {

	return writers != null && visibilityType != null
		&& (visibilityType == ProtocolVisibilityType.TOTAL || readers != null && readers.size() > 0);

    }

    public boolean isProtocolNumberValid() {
	for (Protocol protocol : ProtocolManager.getInstance().getProtocols()) {
	    if (protocol.equals(this.getProtocol()))
		continue;
	    if (protocol.getProtocolNumber().equals(getProtocolNumber())) {
		return false;
	    }
	}
	return true;
    }

    public ProtocolResponsibleBean getBeanForUnit(Unit unit) {

	for (ProtocolResponsibleBean bean : getInternalResponsibles()) {
	    if (unit.equals(bean.getUnit())) {
		return bean;
	    }
	}
	for (ProtocolResponsibleBean bean : getExternalResponsibles()) {
	    if (unit.equals(bean.getUnit())) {
		return bean;
	    }
	}
	return null;
    }

    public void removeUnit(Unit unit) {
	ProtocolResponsibleBean bean = getBeanForUnit(unit);
	removeResponsible(bean);
	if (internalResponsibles != null)
	    internalResponsibles.remove(bean);
	if (externalResponsibles != null)
	    externalResponsibles.remove(bean);
    }

}
