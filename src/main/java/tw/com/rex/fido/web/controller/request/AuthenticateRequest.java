package tw.com.rex.fido.web.controller.request;

import lombok.Data;

@Data
public class AuthenticateRequest {
    private String id;
    private String rawId;
    private Response response;
    private String type;
    private String username;

    @Data
    public static class Response {
        private String clientDataJSON;
        private String authenticatorData;
        private String signature;
    }
}
