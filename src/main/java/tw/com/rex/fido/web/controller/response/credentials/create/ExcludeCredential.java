package tw.com.rex.fido.web.controller.response.credentials.create;

import lombok.Data;

@Data
public class ExcludeCredential {
    // TODO 屬性正確？
    private String id;
    private String type;
    private String transports;
}
