package module.protocols.domain;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.util.CoreConfiguration;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class ProtocolFile extends ProtocolFile_Base {

    public ProtocolFile(Protocol protocol, String filename, byte[] content) {
        super();
        init(filename, filename, content);
        setProtocol(protocol);
    }

    @Override
    public boolean isAccessible(User user) {
        return getProtocol().canFilesBeReadByUser(user);
    }

    @Override
    @Atomic(mode = TxMode.WRITE)
    public void delete() {
        setProtocol(null);
        super.delete();
    }

    public String getDownloadUrl() {
        return CoreConfiguration.getConfiguration().applicationUrl() + "/downloadFile/" + getExternalId() + "/" + getFilename();
    }

}
