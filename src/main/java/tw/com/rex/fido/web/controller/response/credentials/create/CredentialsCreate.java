package tw.com.rex.fido.web.controller.response.credentials.create;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import tw.com.rex.fido.web.controller.response.fido.PreregisterResponse;

import java.util.List;

@Data
public class CredentialsCreate {
    private String attestation;
    private String challenge;
    private AuthenticatorSelection authenticatorSelection;
    private User user;
    private Rp rp;
    private List<ExcludeCredential> excludeCredentials;
    private List<PubKeyCredParam> pubKeyCredParams;
    private int timeout;

    public CredentialsCreate(PreregisterResponse preregister) {
        BeanUtils.copyProperties(preregister, this);
    }
}
