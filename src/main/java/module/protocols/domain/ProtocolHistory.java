package module.protocols.domain;

import java.util.Comparator;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

import com.google.common.collect.Ordering;

/**
 * 
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class ProtocolHistory extends ProtocolHistory_Base {

    public static Comparator<ProtocolHistory> COMPARATOR_BY_BEGIN_DATE = new Ordering<ProtocolHistory>() {
        @Override
        public int compare(ProtocolHistory o1, ProtocolHistory o2) {
            return o1.getBeginDate().compareTo(o2.getBeginDate());
        }
    };

    public static Comparator<ProtocolHistory> COMPARATOR_BY_END_DATE = new Ordering<ProtocolHistory>() {
        @Override
        public int compare(ProtocolHistory o1, ProtocolHistory o2) {
            if (o1.getEndDate() == null) {
                return 1;
            }
            if (o2.getEndDate() == null) {
                return -1;
            }
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

    @Atomic
    public void editProtocolHistory(LocalDate beginDate, LocalDate endDate) {
        this.setBeginDate(beginDate);
        this.setEndDate(endDate);
    }

    public boolean isActive() {
        return getInterval().containsNow();
    }

    public boolean isPast() {
        return getEndDate() != null && getEndDate().toDateTimeAtStartOfDay().isBeforeNow();
    }

    public Interval getInterval() {
        LocalDate startDate = getBeginDate();
        LocalDate endDate = getEndDate();
        long start = startDate == null ? Long.MIN_VALUE : startDate.toDateTimeAtStartOfDay().getMillis();
        long end =
                endDate == null ? Long.MAX_VALUE : endDate.toDateTimeAtStartOfDay().toDateTime().withTime(23, 59, 59, 999)
                        .getMillis();

        return new Interval(start, end);
    }

    public void delete() {
        setProtocol(null);
        deleteDomainObject();
    }

}
