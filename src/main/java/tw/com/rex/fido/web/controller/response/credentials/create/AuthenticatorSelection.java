package tw.com.rex.fido.web.controller.response.credentials.create;

import lombok.Data;

@Data
public class AuthenticatorSelection {
    private boolean requireResidentKey;
    private String userVerification;
    private String authenticatorAttachment;
}
