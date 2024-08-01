package sesac.docshelper.domain.member.dto;

public record IdentityCardInfoDTO(
    String foreignNumber, //등록번호
    String name, //이름
    String country, //국가
    String status, //체류자격
    String issueDate, //발급일자
    String startDateOfStay, //체류기간 허가일
    String endDateOfStay, //체류기간 만료일
    String address, //체류지
    String reportDate //신고일
) {
}
