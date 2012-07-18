/**
 * 
 */
package module.protocols.dto;

import module.protocols.domain.Protocol;
import pt.ist.bennu.core.util.FileUploadBean;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class ProtocolFileUploadBean extends FileUploadBean {

    /**
     * 
     */
    private static final long serialVersionUID = 3510997078215680526L;

    private final Protocol protocol;

    private final String protocolNumber;

    public ProtocolFileUploadBean(Protocol protocol) {
	super();
	this.protocol = protocol;
	this.protocolNumber = protocol.getProtocolNumber();
    }

    public Protocol getProtocol() {
	return protocol;
    }

    public String getProtocolNumber() {
	return protocolNumber;
    }

}
