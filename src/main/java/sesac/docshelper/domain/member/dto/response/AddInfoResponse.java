package sesac.docshelper.domain.member.dto.response;

public record AddInfoResponse (
    long id,
    String email,
    String name,
    Long income,
    String housingType,
    String phoneNumber,
    String currentWorkplace,
    String currentWorkplaceRegistrationNumber,
    String workplacePhoneNumber,
    String profile,
    String sign
) {}
