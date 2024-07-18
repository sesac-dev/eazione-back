package sesac.docshelper.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //security
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "멤버가 존재하지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰 입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 입니다."),

    // S3
    CANT_UPLOAD_FILE(HttpStatus.BAD_REQUEST, "해당 파일을 업로드할 수가 없습니다.")
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
