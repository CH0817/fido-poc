package tw.com.rex.fido.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "fido")
public class FidoServerProperties {

    private String url;
    private Map<String, String> headers;

    public String preregisterUrl() {
        return url + "/api/preregister";
    }

    public String registerUrl() {
        return url + "/api/register";
    }

    public String preauthenticateUrl() {
        return url + "/api/preauthenticate";
    }

    public String  authenticateUrl() {
        return url + "/api/authenticate";
    }
}
