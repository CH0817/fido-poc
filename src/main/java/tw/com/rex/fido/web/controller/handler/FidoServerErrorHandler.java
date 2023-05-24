package tw.com.rex.fido.web.controller.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import tw.com.rex.fido.exception.FidoServerException;
import tw.com.rex.fido.web.controller.response.fido.RegisterResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
public class FidoServerErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        HttpStatus.Series series = response.getStatusCode().series();
        return HttpStatus.Series.CLIENT_ERROR.equals(series) || HttpStatus.Series.SERVER_ERROR.equals(series);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = responseBodyToString(response.getBody());
        log.error("FIDO Server 回傳錯誤訊息: {}", responseBody);
        RegisterResponse registerResponse = objectMapper.readValue(responseBody, RegisterResponse.class);
        throw new FidoServerException(registerResponse);
    }

    private String responseBodyToString(InputStream responseBody) {
        return new BufferedReader(new InputStreamReader(responseBody, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    }
}
