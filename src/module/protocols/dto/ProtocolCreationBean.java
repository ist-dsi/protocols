package module.protocols.dto;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 *
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import module.organization.domain.Person;
import module.organization.domain.Unit;
import module.protocols.domain.Protocol;
import module.protocols.domain.ProtocolHistory;
import module.protocols.domain.ProtocolManager;
import module.protocols.domain.ProtocolResponsible;
import module.protocols.domain.util.ProtocolAction;
import module.protocols.domain.util.ProtocolActionType;
import module.protocols.domain.util.ProtocolResponsibleType;
import myorg.applicationTier.Authenticate;
import myorg.domain.groups.PersistentGroup;

import org.joda.time.LocalDate;

public class ProtocolCreationBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3819698830239113842L;

    public static class ProtocolResponsibleBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1303497805898307379L;

	private final Unit unit;

	private final List<Person> responsibles;

	private Person personToRemove;

	public ProtocolResponsibleBean(ProtocolResponsible responsible) {
	    this.unit = responsible.getUnit();
	    this.responsibles = new ArrayList<Person>(responsible.getPeople());
	}

	public ProtocolResponsibleBean(Unit unit) {
	    super();
	    this.unit = unit;
	    this.responsibles = new ArrayList<Person>();
	}

	public Unit getUnit() {
	    return unit;
	}

	public List<Person> getResponsibles() {
	    return responsibles;
	}

	public void addResponsible(Person responsible) {
	    if (responsible != null && !responsibles.contains(responsible))
		responsibles.add(responsible);
	}

	public boolean check() {
	    return unit != null && responsibles.size() > 0;
	}

	/**
	 * @param newPerson
	 */
	public void removeResponsible(Person newPerson) {
	    responsibles.remove(newPerson);
	}

	public Person getPersonToRemove() {
	    return personToRemove;
	}

	public void setPersonToRemove(Person personToRemove) {
	    this.personToRemove = personToRemove;
	}
    }

    private Protocol protocol;

    private ProtocolAction protocolAction;

    private List<ProtocolHistory> protocolHistories;

    /*
     * Step 1
     */

    // TODO Remove default initializations, for testing purposes only!

    private String protocolNumber = "protocol";

    private LocalDate signedDate = new LocalDate();

    private LocalDate beginDate;

    private LocalDate endDate;

    private String scientificAreas;

    private List<ProtocolActionType> actionTypes;

    private String otherActionTypes;

    private String observations;

    /*
     * Step 2
     */

    private List<ProtocolResponsibleBean> internalResponsibles;

    private Unit newUnit;

    private Person newPerson;

    /*
     * Step 3
     */

    private List<ProtocolResponsibleBean> externalResponsibles;

    /*
     * Step 4
     */

    private List<PersistentGroup> allowedToView;

    private List<PersistentGroup> allowedToEdit;

    /*
     * Extra stuff
     */

    private Boolean renewable;

    private Boolean active;

    public ProtocolCreationBean() {

    }

    public ProtocolCreationBean(Protocol protocol) {
	setProtocol(protocol);
	setProtocolNumber(protocol.getProtocolNumber());
	setSignedDate(protocol.getSignedDate());
	setObservations(protocol.getObservations());
	setRenewable(protocol.getRenewable());
	setActive(protocol.getActive());
	setProtocolHistories(new ArrayList<ProtocolHistory>(protocol.getProtocolHistories()));
	setProtocolAction(protocol.getProtocolAction());
	setActionTypes(new ArrayList<ProtocolActionType>(protocol.getProtocolAction().getProtocolActionTypes()));
	setOtherActionTypes(protocol.getProtocolAction().getOtherTypes());
	setScientificAreas(protocol.getScientificAreas());

	for (ProtocolResponsible responsible : protocol.getProtocolResponsible()) {
	    ProtocolResponsibleBean bean = new ProtocolResponsibleBean(responsible);
	    if (responsible.getType() == ProtocolResponsibleType.INTERNAL) {
		addInternalResponsible(bean);
	    } else {
		addExternalResponsible(bean);
	    }
	}
    }

    public Protocol getProtocol() {
	return protocol;
    }

    public void setProtocol(Protocol protocol) {
	this.protocol = protocol;
    }

    public ProtocolAction getProtocolAction() {
	return protocolAction;
    }

    public void setProtocolAction(ProtocolAction protocolAction) {
	this.protocolAction = protocolAction;
    }

    public List<ProtocolHistory> getProtocolHistories() {
	return protocolHistories;
    }

    public void setProtocolHistories(List<ProtocolHistory> protocolHistories) {
	this.protocolHistories = protocolHistories;
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

    public List<ProtocolResponsibleBean> getInternalResponsibles() {
	return internalResponsibles;
    }

    public void setInternalResponsibles(List<ProtocolResponsibleBean> internalResponsibles) {
	this.internalResponsibles = internalResponsibles;
    }

    public List<ProtocolResponsibleBean> getExternalResponsibles() {
	return externalResponsibles;
    }

    public void setExternalResponsibles(List<ProtocolResponsibleBean> externalResponsibles) {
	this.externalResponsibles = externalResponsibles;
    }

    public Boolean getRenewable() {
	return renewable;
    }

    public void setRenewable(Boolean renewable) {
	this.renewable = renewable;
    }

    public Boolean getActive() {
	return active;
    }

    public void setActive(Boolean active) {
	this.active = active;
    }

    public List<PersistentGroup> getAllowedToView() {
	return allowedToView;
    }

    public void setAllowedToView(List<PersistentGroup> allowedToView) {
	this.allowedToView = allowedToView;
    }

    public List<PersistentGroup> getAllowedToEdit() {
	return allowedToEdit;
    }

    public void setAllowedToEdit(List<PersistentGroup> allowedToEdit) {
	this.allowedToEdit = allowedToEdit;
    }

    public void addInternalResponsible(ProtocolResponsibleBean bean) {
	if (internalResponsibles == null)
	    internalResponsibles = new ArrayList<ProtocolResponsibleBean>();

	internalResponsibles.add(bean);
    }

    public void addExternalResponsible(ProtocolResponsibleBean bean) {
	if (externalResponsibles == null)
	    externalResponsibles = new ArrayList<ProtocolResponsibleBean>();

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

    public Person getCreator() {

	// TODO Check this

	return Authenticate.getCurrentUser().getPerson();
    }

    /**
     * Verifies whether the internal responsibles are correctly set
     * 
     * @return
     */
    public boolean internalResponsiblesCorrect() {

	if (internalResponsibles != null || internalResponsibles.size() > 0) {
	    for (ProtocolResponsibleBean bean : internalResponsibles) {
		if (!bean.check())
		    return false;
	    }
	}

	return true;
    }

    /**
     * Verifies whether the external responsibles are correctly set
     * 
     * @return
     */
    public boolean externalResponsiblesCorrect() {

	if (externalResponsibles != null || externalResponsibles.size() > 0) {
	    for (ProtocolResponsibleBean bean : externalResponsibles) {
		if (!bean.check())
		    return false;
	    }
	}

	return true;
    }

    public boolean permissionsCorrectlyDefined() {

	return allowedToEdit != null && allowedToEdit.size() > 0 && allowedToView != null && allowedToView.size() > 0;
    }

    public boolean isProtocolNumberValid() {
	for (Protocol protocol : ProtocolManager.getInstance().getProtocols()) {
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
	return null;
    }

}