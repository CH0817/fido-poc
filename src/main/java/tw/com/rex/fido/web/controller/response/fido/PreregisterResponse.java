package tw.com.rex.fido.web.controller.response.fido;

import lombok.Data;
import tw.com.rex.fido.web.controller.response.credentials.create.*;

import java.util.List;

@Data
public class PreregisterResponse  {
    private String traceId;
    private String status;
    private String errorMessage;
    private String attestation;
    private String challenge;
    private AuthenticatorSelection authenticatorSelection;
    private User user;
    private Rp rp;
    private List<ExcludeCredential> excludeCredentials;
    private List<PubKeyCredParam> pubKeyCredParams;
    private int timeout;
}
