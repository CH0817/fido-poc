package tw.com.rex.fido.web.controller.response.fido;

import lombok.Data;

@Data
public class AuthenticateResponse {
    private String status;
    private String errorMessage;
}
