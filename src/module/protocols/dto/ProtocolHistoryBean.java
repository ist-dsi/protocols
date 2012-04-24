/**
 * 
 */
package module.protocols.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.protocols.domain.Protocol;
import module.protocols.domain.Protocol.RenewTime;
import module.protocols.domain.ProtocolHistory;

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

    public ProtocolHistoryBean(Protocol protocol) {
	setProtocol(protocol);
    }

    private Integer duration;

    private RenewTime renewTime;

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

    public List<ProtocolHistory> getProtocolHistories() {
	List<ProtocolHistory> histories = new ArrayList<ProtocolHistory>(protocol.getProtocolHistories());

	Collections.sort(histories, ProtocolHistory.COMPARATOR_BY_BEGIN_DATE);

	return histories;
    }

}
