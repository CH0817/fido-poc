package tw.com.rex.fido.util;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tw.com.rex.fido.web.controller.handler.FidoServerErrorHandler;

@Component
public class FidoRestTemplate extends RestTemplate {
    public FidoRestTemplate() {
        setErrorHandler(new FidoServerErrorHandler());
    }
}
