package sesac.docshelper.domain.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sesac.docshelper.domain.member.dto.TokenDTO;
import sesac.docshelper.domain.member.dto.googleOauth.GoogleOauthDTO;
import sesac.docshelper.domain.member.dto.googleOauth.GoogleProfileDTO;
import sesac.docshelper.domain.member.dto.response.SignUpResponse;
import sesac.docshelper.domain.member.entity.Member;
import sesac.docshelper.domain.member.repository.MemberRepository;
import sesac.docshelper.global.dto.response.ResultResponse;
import sesac.docshelper.global.util.jwt.JwtUtil;

import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class MemberOAuth2Service {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Value("${oauth2.google.client-id}")
    private String client_id;
    @Value("${oauth2.google.client-secret}")
    private String client_secret;

    public SignUpResponse signUp(String authorization_code, String redirectUri){
        return Optional.ofNullable(requestATK(authorization_code, redirectUri))
                .map(oauthDTO -> findProfile(oauthDTO.access_token()))
                .map(profileDTO -> memberRepository.findByEmail(profileDTO.email())
                        .orElseGet(() -> memberRepository.save(Member.newbi(profileDTO.email(), profileDTO.name()))))
                .map(member -> new SignUpResponse(member.getId(), member.getEmail(), member.getName(), getTokenDTO(member.getEmail())))
                .orElse(null);
    }


    private GoogleOauthDTO requestATK(String code, String redirectUri) {
        // 메타 데이터 작성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        log.info("{}", client_id);
        log.info("{}", redirectUri);
        // 요청 Params 작성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); // 고정
        params.add("client_id", client_id);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("client_secret", client_secret);
        // 요청에 대한 Entity 만들기
        HttpEntity<MultiValueMap<String,String>> requestEntity = new HttpEntity<>(params, headers);
        // AccessToken 요청 후 결과값 받기
        ResponseEntity<String> responseEntity = new RestTemplate().exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        // 받은 직렬화된 JSON 데이터를 역직렬화(객체화) 하기 위해 ObjectMapper를 사용.
        // ObjectMapper의 정책 설정: JSON에는 존재하지만, Java 객체에는 존재하지 않는 속성에 대해 무시하고 역직렬화 진행 (원래는 에러남)
        try{
            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            log.info("성공적으로 GOOGLE과 통신했습니다.={}", responseEntity.getBody());
            return objectMapper.readValue(responseEntity.getBody(), GoogleOauthDTO.class);
        }catch (JsonProcessingException e) {
            log.info(e.getMessage());
            return null;
        }
    }

    private GoogleProfileDTO findProfile(String token) {
        // restTemplate 작성
        RestTemplate rt = new RestTemplate();
        // Meta data 작성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        // Http Entity 만들기
        HttpEntity<MultiValueMap<String, String>> googleProfileRequest = new HttpEntity<>(headers);
        // Http 요청 후, response 변수에 응답을 받는다.
        ResponseEntity<String> googleProfileResponse = rt.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                googleProfileRequest,
                String.class
        );
        // 받은 값 역직렬화 후 객체로 정제
        try{
            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            GoogleProfileDTO googleProfileDTO = null;
            googleProfileDTO = objectMapper.readValue(googleProfileResponse.getBody(), GoogleProfileDTO.class);
            return googleProfileDTO;
        }catch (JsonProcessingException e){
            log.info(e.getMessage());
            return null;
        }
    }

    private TokenDTO getTokenDTO(String email) {
        return new TokenDTO(jwtUtil.createToken(email, true), jwtUtil.createToken(email, false));
    }
}
