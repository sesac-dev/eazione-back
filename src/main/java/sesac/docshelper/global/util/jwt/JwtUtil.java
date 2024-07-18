package sesac.docshelper.global.util.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sesac.docshelper.global.util.UserDetailsServiceImpl;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    @Value("${jwt.secret}")
    private String ingredient;
    private Key key;

    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
    @Value("#{new Integer('${jwt.token.access-expiration-time}')}")
    private Integer accessTokenTime;
    @Value("#{new Integer('${jwt.token.refresh-expiration-time}')}")
    private Integer refreshTokenTime;
    private static final String BEARER_PREFIX = "Bearer ";

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(ingredient);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // Bearer 글자 분리
    public String separateBearer(String bearerToken) {
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
    // 토큰 생성
    public String createToken(String email, boolean isAccess) {
        Date now = new Date();
        // 토근 생성
        return Jwts.builder()
                .setSubject(String.valueOf(email))  // 토큰 주인은 누구 인지
                .setIssuedAt(new Date(now.getTime())) // 토큰 발행자
                .setExpiration(                       // 토큰 만료일
                        new Date(now.getTime() + (isAccess? accessTokenTime : refreshTokenTime))
                )
                .signWith(key, signatureAlgorithm)  // C part Key 생성
                .compact();
    }

    // 토큰 유효성 검증
    public int validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return 0;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
            log.error(e.getMessage());
            return -1;
        } catch (io.jsonwebtoken.security.SignatureException | SecurityException |
                 MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            log.error(e.getMessage());
            return -2;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
            log.error(e.getMessage());
            return -3;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            log.error(e.getMessage());
            return -4;
        }
    }
    // 토큰 해부해서 payload 받기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
    // email에 해당하는 회원 정보를 DB에서 가져와서 인증 객체를 생성
    public Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
