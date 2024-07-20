package sesac.docshelper.domain.member.dto.response;

public record IdCardFrontResponse (
        String number,
        String name,
        String country,
        String status,
        String issueDate
) {
}
