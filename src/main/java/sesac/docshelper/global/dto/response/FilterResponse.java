package sesac.docshelper.global.dto.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import sesac.docshelper.global.exception.ErrorCode;

import java.io.IOException;
import java.util.HashMap;

@Component
public class FilterResponse {

    private final ObjectMapper mapper = new ObjectMapper();

    public <T> HttpServletResponse ok(HttpServletResponse response, T data) throws IOException {

        HashMap<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", "success");
        responseBody.put("message", "처리 성공");
        responseBody.put("data", data);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(mapper.writeValueAsString(responseBody));

        return response;
    }

    public void error(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        HashMap<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", "fail");
        responseBody.put("message", errorCode.getMessage());
        responseBody.put("data", null);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(responseBody));
        response.setStatus(errorCode.getHttpStatus().value());
    }

    public void error(HttpServletResponse response, String msg) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, msg);
    }

}
