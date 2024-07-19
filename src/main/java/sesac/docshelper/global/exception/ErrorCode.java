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
    // Google
    BAD_REQUEST_TO_GOOGLE(HttpStatus.BAD_REQUEST, "Redirect_type_misMatch"),
    // S3
    CANT_UPLOAD_FILE(HttpStatus.BAD_REQUEST, "해당 파일을 업로드할 수가 없습니다."),
    // docs
    VALID_DOCS_TYPE(HttpStatus.BAD_REQUEST, "올바르지 않은 서류 유형입니다."),
    BAD_REQUET_TO_DOCS(HttpStatus.BAD_REQUEST, "서류 추가 요청이 잘못되었습니다.")
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
