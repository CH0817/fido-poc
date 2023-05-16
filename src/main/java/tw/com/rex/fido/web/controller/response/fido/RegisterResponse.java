package tw.com.rex.fido.web.controller.response.fido;

import lombok.Data;

@Data
public class RegisterResponse {
    private String errorMessage;
    private String status;
    private String code;
    private String message;
}
