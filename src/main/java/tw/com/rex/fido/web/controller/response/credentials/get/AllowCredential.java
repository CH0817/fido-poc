package tw.com.rex.fido.web.controller.response.credentials.get;

import lombok.Data;

@Data
public class AllowCredential {
    private String id;
    private String type;
    private String transports;
}
