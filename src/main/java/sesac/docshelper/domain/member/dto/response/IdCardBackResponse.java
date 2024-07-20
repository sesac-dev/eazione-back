package sesac.docshelper.domain.member.dto.response;

public record IdCardBackResponse (
        String startDateOfStay,
        String endDateOfStay,
        String address,
        String reportDate
) {

}
