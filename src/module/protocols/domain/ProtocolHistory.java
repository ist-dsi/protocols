package module.protocols.domain;

import java.util.Comparator;

import myorg.domain.exceptions.DomainException;
import myorg.util.IntervalTools;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class ProtocolHistory extends ProtocolHistory_Base {

    public static Comparator<ProtocolHistory> COMPARATOR_BY_BEGIN_DATE = new Comparator<ProtocolHistory>() {
	@Override
	public int compare(ProtocolHistory o1, ProtocolHistory o2) {
	    return o1.getBeginDate().compareTo(o2.getBeginDate());
	}
    };

    public static Comparator<ProtocolHistory> COMPARATOR_BY_END_DATE = new Comparator<ProtocolHistory>() {
	@Override
	public int compare(ProtocolHistory o1, ProtocolHistory o2) {
	    return o1.getEndDate().compareTo(o2.getEndDate());
	}
    };

    private ProtocolHistory() {
	super();
    }

    public ProtocolHistory(Protocol protocol, LocalDate beginDate, LocalDate endDate) {
	this();
	super.setTimestamp(new DateTime());
	setProtocol(protocol);
	setBeginDate(beginDate);
	setEndDate(endDate);
    }

    @Service
    public void editProtocolHistory(LocalDate beginDate, LocalDate endDate) {
	this.setBeginDate(beginDate);
	this.setEndDate(endDate);
    }

    @Override
    public void setTimestamp(DateTime dateTime) {
	throw new DomainException("protocolHistory.changeTimestamp.notAllowed");
    }

    public boolean isActive() {
	return getInterval().containsNow();
    }

    public Interval getInterval() {
	return IntervalTools.getInterval(getBeginDate(), getEndDate());
    }

    public void delete() {
	removeProtocol();
	deleteDomainObject();
    }

}
