package sesac.docshelper.domain.member.dto;

public record TokenDTO (
        String accessToken,
        String refreshToken
) {}
