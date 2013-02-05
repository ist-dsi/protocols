/**
 * 
 */
package module.protocols.dto;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import module.fileManagement.domain.FileNode;
import module.fileManagement.presentationTier.DownloadUtil;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class ProtocolFileBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5277452798674241974L;

    private final FileNode file;

    private final String fileURL;

    public ProtocolFileBean(FileNode file, HttpServletRequest request) {
        this.file = file;
        this.fileURL = DownloadUtil.getDownloadUrl(request, file.getDocument().getLastVersionedFile());
    }

    public FileNode getFile() {
        return file;
    }

    public String getFileURL() {
        return fileURL;
    }

}
