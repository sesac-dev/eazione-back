package sesac.docshelper.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResultResponse<T> {    //

    private String status;
    private String message;
    private T data;

    public static <T> ResultResponse<T> success(T data) {
        return new ResultResponse<>("success", "API 상태 양호 ", data);
    }

    public static <T> ResultResponse<T> fail(String message) {
        return new ResultResponse<>("fail", message, null);
    }

}
