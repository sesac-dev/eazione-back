package sesac.docshelper.global.util.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sesac.docshelper.global.exception.ErrorCode;
import sesac.docshelper.global.util.ConstantUtil;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 토큰 가져오기
        String token = jwtUtil.separateBearer(request.getHeader("Authorization"));
        // 토큰이 없다면 다음 filter로 넘기기
        if(token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        // 토큰 유효성 체크 -> 통과 못하면 바로 다음 filter로 넘어간다.
        switch (jwtUtil.validateToken(token)) {
            case -1:
                request.setAttribute(ConstantUtil.EXCEPTION_ATTRIBUTE, ErrorCode.EXPIRED_TOKEN);
                filterChain.doFilter(request, response);
                return;
            case -2:
            case -3:
            case -4:
                request.setAttribute(ConstantUtil.EXCEPTION_ATTRIBUTE, ErrorCode.INVALID_TOKEN);
                filterChain.doFilter(request, response);
                return;
        }
        // 토큰으로 인증 객체 만들기
        Claims info = jwtUtil.getUserInfoFromToken(token);
        log.info("Token에 들어 있는 값={}", info.toString());
        try {
            setAuthentication(info.getSubject());
        } catch (UsernameNotFoundException e) {
            request.setAttribute(ConstantUtil.EXCEPTION_ATTRIBUTE, ErrorCode.MEMBER_NOT_FOUND.getMessage());
            log.error("관련 에러: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
    // 인증 객체 생성 후 stateless 한 세션에 등록
    private void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(email);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
