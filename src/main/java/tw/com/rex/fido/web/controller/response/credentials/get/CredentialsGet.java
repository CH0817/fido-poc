package tw.com.rex.fido.web.controller.response.credentials.get;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import tw.com.rex.fido.web.controller.response.fido.PreauthenticateResponse;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CredentialsGet {
    private String userVerification;
    private String challenge;
    private String rpId;
    private List<AllowCredential> allowCredentials;
    private int timeout;

    public CredentialsGet(PreauthenticateResponse preauthenticate) {
        BeanUtils.copyProperties(preauthenticate, this);
    }
}
