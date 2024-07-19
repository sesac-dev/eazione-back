package sesac.docshelper.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class RequestBodyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if ("POST".equalsIgnoreCase(request.getMethod()) && request.getRequestURI().contains("/api")) {
            BufferedReader reader = request.getReader();
            String requestBody = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            System.out.println("Request Body: " + requestBody);
        }
        return true;
    }
}

