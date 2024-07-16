package sesac.docshelper.domain.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class IdentityCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String number; //등록번호
    String name; //이름
    String county; //국가
    String status;  //체류자격
    String issueDate; //발급일자
    String startDateOfStay; //체류기간 허가일
    String endDateOfStay; //체류기간 만료일
    String address; //체류지
    String reportDate; //신고일
}