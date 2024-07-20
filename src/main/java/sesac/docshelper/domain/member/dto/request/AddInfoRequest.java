package sesac.docshelper.domain.member.dto.request;

public record AddInfoRequest(
        long income,
        String housingType,
        String phoneNumber
) {
}
