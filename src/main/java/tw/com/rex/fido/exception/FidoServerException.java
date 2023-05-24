package tw.com.rex.fido.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tw.com.rex.fido.web.controller.response.fido.RegisterResponse;

@Data
@EqualsAndHashCode(callSuper = false)
public class FidoServerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private RegisterResponse registerResponse;

    public FidoServerException(RegisterResponse registerResponse) {
        super("FIDO server 發生錯誤");
        this.registerResponse = registerResponse;
    }
}
