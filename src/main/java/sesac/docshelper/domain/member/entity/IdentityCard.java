package sesac.docshelper.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class IdentityCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String foreignNumber; //등록번호
    String name; //이름
    String country; //국가
    String status;  //체류자격
    String issueDate; //발급일자
    String startDateOfStay; //체류기간 허가일
    String endDateOfStay; //체류기간 만료일
    String address; //체류지
    String reportDate; //신고일

    @OneToOne
    @JsonManagedReference
    Member member;
}
