package sesac.docshelper.global.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sesac.docshelper.global.dto.response.ResultResponse;


@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private static final HttpHeaders jsonHeaders;

    static {
        jsonHeaders = new HttpHeaders();
        jsonHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ResultResponse<Object>> globalExceptionHandler(GlobalException globalException) {
        ResultResponse<Object> response = ResultResponse.fail(globalException.getErrorCode().getMessage());
        return new ResponseEntity<>(response, jsonHeaders, globalException.getErrorCode().getHttpStatus());
    }

}
