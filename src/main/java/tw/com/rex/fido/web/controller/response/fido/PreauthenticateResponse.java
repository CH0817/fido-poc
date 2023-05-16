package tw.com.rex.fido.web.controller.response.fido;

import lombok.Data;
import tw.com.rex.fido.web.controller.response.credentials.get.AllowCredential;

import java.util.List;

@Data
public class PreauthenticateResponse {
    private String traceId;
    private String status;
    private String errorMessage;
    private String userVerification;
    private String challenge;
    private String rpId;
    private List<AllowCredential> allowCredentials;
    private int timeout;
}
