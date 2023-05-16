package tw.com.rex.fido.web.controller.response.credentials.create;

import lombok.Data;

@Data
public class PubKeyCredParam {
    private String type;
    private int alg;
}
