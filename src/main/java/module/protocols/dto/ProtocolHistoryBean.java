/**
 * 
 */
package module.protocols.dto;

import java.io.Serializable;
import java.util.List;

import module.protocols.domain.Protocol;
import module.protocols.domain.Protocol.RenewTime;
import module.protocols.domain.ProtocolHistory;

import org.joda.time.LocalDate;

import com.google.common.collect.Ordering;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */

public class ProtocolHistoryBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5816255710886226533L;

    private Protocol protocol;

    private LocalDate beginDate;

    private LocalDate endDate;

    private Integer duration;

    private RenewTime renewTime;

    private ProtocolHistory protocolHistory;

    public ProtocolHistoryBean(Protocol protocol) {
	setProtocol(protocol);
    }

    public ProtocolHistoryBean(ProtocolHistory history) {
	this.protocolHistory = history;
	this.beginDate = history.getBeginDate();
	this.endDate = history.getEndDate();
    }

    public Integer getDuration() {
	return duration;
    }

    public void setDuration(Integer duration) {
	this.duration = duration;
    }

    public RenewTime getRenewTime() {
	return renewTime;
    }

    public void setRenewTime(RenewTime renewTime) {
	this.renewTime = renewTime;
    }

    public Protocol getProtocol() {
	return protocol;
    }

    public void setProtocol(Protocol protocol) {
	if (protocol != null) {
	    this.protocol = protocol;
	} else {
	    this.protocol = null;
	}
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

    public ProtocolHistory getProtocolHistory() {
	return protocolHistory;
    }

    public List<ProtocolHistory> getProtocolHistories() {

	return Ordering.from(ProtocolHistory.COMPARATOR_BY_BEGIN_DATE).sortedCopy(protocol.getProtocolHistories());

    }

}
