/**
 * 
 */
package module.protocols.domain;

/**
 * Defines the level of confidentiality granted to the protocol.
 * 
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public enum ProtocolVisibilityType {

    /**
     * The whole protocol will be accessible to everyone.
     */
    NONE,

    /**
     * The protocol meta-data will be publicly accessible, but access to the
     * protocol files will be conditioned.
     */
    FILES,

    /**
     * The protocol meta-data and its files will be accessible only to those
     * specified in the access control list.
     */
    PROTOCOL;

}
