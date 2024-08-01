package sesac.docshelper.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sesac.docshelper.domain.docs.entity.NomalDocs;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String email;
    String name;
    //추가 정보
    Long income; //소득
    String housingType; //주거형태
    String phoneNumber; //전화번호
    String currentWorkplace; //현근무처
    String currentWorkplaceRegistrationNumber;//현근무처_등록번호
    String workplacePhoneNumber;//현근무처_전화번호
    @Column(length = 2048)
    String profile;
    @Column(length = 2048)
    String sign;

    // 매핑
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonBackReference
    private IdentityCard identityCard;
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonBackReference // 해당 Entity는 Member가 관리하고 있는 Entity이다.
    private Passport passport;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<NomalDocs> nomalDocsList;

    @Builder
    public Member(String email, String name, long income, String housingType, String phoneNumber){
        this.email = email;
        this.name  = name;
        this.income =income;
        this.housingType = housingType;
        this.phoneNumber = phoneNumber;
    }

    public static Member newbi(String email, String name){
        return Member.builder()
                .email(email)
                .name(name)
                .income(0)
                .housingType(null)
                .phoneNumber("0")
                .build();
    }
}
