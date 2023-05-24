package tw.com.rex.fido.web.controller.response.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BaseResponse<T> {

    private String code;
    private T data;

    private BaseResponse() {
        // do nothing
    }

    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> result = new BaseResponse<>();
        result.setCode("200");
        result.setData(data);
        return  result;
    }

    public static <T> BaseResponse<T> fail(T data) {
        BaseResponse<T> result = new BaseResponse<>();
        result.setCode("500");
        result.setData(data);
        return  result;
    }

}
