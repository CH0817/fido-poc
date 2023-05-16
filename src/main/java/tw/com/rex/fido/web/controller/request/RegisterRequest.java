package tw.com.rex.fido.web.controller.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String id;
    private String rawId;
    private Response response;
    private String type;
    private String username;
    private String userId;
//    private String authenticatorAttachment;

    @Data
    public static class Response {
        private String clientDataJSON;
        private String attestationObject;
    }
}
