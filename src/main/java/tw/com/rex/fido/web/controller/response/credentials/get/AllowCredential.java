package tw.com.rex.fido.web.controller.response.credentials.get;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AllowCredential {
    private String id;
    private String type;
    private String transports;
}
