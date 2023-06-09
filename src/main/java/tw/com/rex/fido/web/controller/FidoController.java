package tw.com.rex.fido.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tw.com.rex.fido.exception.FidoServerException;
import tw.com.rex.fido.properties.FidoServerProperties;
import tw.com.rex.fido.util.FidoRestTemplate;
import tw.com.rex.fido.web.controller.request.AuthenticateRequest;
import tw.com.rex.fido.web.controller.request.PreauthenticateRequest;
import tw.com.rex.fido.web.controller.request.PreregisterRequest;
import tw.com.rex.fido.web.controller.request.RegisterRequest;
import tw.com.rex.fido.web.controller.response.base.BaseResponse;
import tw.com.rex.fido.web.controller.response.credentials.create.CredentialsCreate;
import tw.com.rex.fido.web.controller.response.credentials.get.CredentialsGet;
import tw.com.rex.fido.web.controller.response.fido.AuthenticateResponse;
import tw.com.rex.fido.web.controller.response.fido.PreauthenticateResponse;
import tw.com.rex.fido.web.controller.response.fido.PreregisterResponse;
import tw.com.rex.fido.web.controller.response.fido.RegisterResponse;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/fido")
public class FidoController {

    private ObjectMapper objectMapper;
    private FidoServerProperties fidoServerProperties;
    private FidoRestTemplate restTemplate;

    @PostMapping(value = "/preregister", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public BaseResponse<CredentialsCreate> preregister(@Validated @RequestBody PreregisterRequest request) throws JsonProcessingException {
        log.info("FIDO server preregister request: {}", objectMapper.writeValueAsString(request));
        PreregisterResponse preregisterResponse = sendRequest(fidoServerProperties.preregisterUrl(), request, PreregisterResponse.class);
        log.info("FIDO server preregister response: {}", objectMapper.writeValueAsString(preregisterResponse));
        return BaseResponse.success(new CredentialsCreate(preregisterResponse));
    }

    @PostMapping(value = "/register", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public BaseResponse<RegisterResponse> register(@RequestBody RegisterRequest request) throws JsonProcessingException {
        log.info("FIDO server register request: {}", objectMapper.writeValueAsString(request));
        RegisterResponse registerResponse = sendRequest(fidoServerProperties.registerUrl(), request, RegisterResponse.class);
        log.info("FIDO server register response: {}", objectMapper.writeValueAsString(registerResponse));
        return BaseResponse.success(registerResponse);
    }

    @PostMapping("/preauthenticate")
    public BaseResponse<CredentialsGet> preauthenticate(@RequestBody PreauthenticateRequest request) throws JsonProcessingException {
        log.debug("FIDO server preauthenticate request: {}", objectMapper.writeValueAsString(request));
        PreauthenticateResponse preauthenticateResponse = sendRequest(fidoServerProperties.preauthenticateUrl(), request, PreauthenticateResponse.class);
        log.debug("FIDO server preauthenticate response: {}", objectMapper.writeValueAsString(request));
        return BaseResponse.success(new CredentialsGet(preauthenticateResponse));
    }

    @PostMapping("/authenticate")
    public BaseResponse<AuthenticateResponse> authenticate(@RequestBody AuthenticateRequest request) throws JsonProcessingException {
        log.debug("FIDO server authenticate request: {}", objectMapper.writeValueAsString(request));
        AuthenticateResponse authenticateResponse = sendRequest(fidoServerProperties.authenticateUrl(), request, AuthenticateResponse.class);
        log.debug("FIDO server authenticate response: {}", objectMapper.writeValueAsString(authenticateResponse));
        return BaseResponse.success(authenticateResponse);
    }

    private <T> T sendRequest(String url, Object request, Class<T> responseType) throws JsonProcessingException {
        return restTemplate.postForEntity(url, httpEntity(request), responseType).getBody();
    }

    private HttpHeaders fidoServerHeaders() {
        HttpHeaders result = new HttpHeaders();
        result.setContentType(MediaType.APPLICATION_JSON);
        fidoServerProperties.getHeaders().forEach(result::add);
        return result;
    }

    private HttpEntity<String> httpEntity(Object request) throws JsonProcessingException {
        return new HttpEntity<>(objectMapper.writeValueAsString(request), fidoServerHeaders());
    }

    @ExceptionHandler(FidoServerException.class)
    public BaseResponse<RegisterResponse> fidoServerExceptionHandler(FidoServerException e) {
        log.error("FidoServerException 錯誤處理", e);
        return BaseResponse.fail(e.getRegisterResponse());
    }

}
