package sesac.docshelper.domain.member.dto.googleOauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public record GoogleOauthDTO (
        String access_token,
        int expires_in,
        String refresh_token,
        String scope,
        String token_type
) { }
