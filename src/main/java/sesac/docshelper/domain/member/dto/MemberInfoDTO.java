package sesac.docshelper.domain.member.dto;

public record MemberInfoDTO(
    long id,
    String email,
    String name,
    Long income,
    String housingType,
    String phoneNumber,
    String currentWorkplace,
    String currentWorkplaceRegistrationNumber,
    String workplacePhoneNumber,
    String profileImage,
    String signature,
    IdentityCardInfoDTO identityCardInfo,
    PassportInfoDTO passportInfo
) {
}
