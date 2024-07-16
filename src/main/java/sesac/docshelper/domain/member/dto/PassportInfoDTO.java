package sesac.docshelper.domain.member.dto;

public record PassportInfoDTO(
        String passportNumber, //여권번호
        String sureName, //성
        String givenNames, //이름
        String dateOfBirth,//생일
        String sex, //성별
        String nationality, //국적
        String dateOfIssue, //발급일
        String expiryOfDate, //만료일
        String issuingAuthority, //발급기관
        String placeOfBirth, //출생지
        String type, //여권종류
        String countryOfIssue //여권 발급국
) {
}
