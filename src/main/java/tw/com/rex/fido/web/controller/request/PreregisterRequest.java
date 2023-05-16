package tw.com.rex.fido.web.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class PreregisterRequest {
    @NotBlank(message = "username不能為空")
    private String username;
    private String displayName;
}
