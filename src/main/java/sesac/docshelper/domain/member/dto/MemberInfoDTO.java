package sesac.docshelper.domain.member.dto;

public record MemberInfoDTO(
        String email,
        String name,
        String profileImage,
        PassportInfoDTO passportInfo,
        IdentityCardInfoDTO identityCardInfo,
        Long income, //소득
        String housingType //주거형태
) {
}
