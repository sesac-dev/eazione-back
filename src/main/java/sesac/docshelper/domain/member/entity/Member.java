package sesac.docshelper.domain.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    // 매핑
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private IdentityCard identityCard;
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Passport passport;

    @Builder
    public Member(String email, String name, long income, String housingType){
        this.email = email;
        this.name  = name;
        this.income =income;
        this.housingType = housingType;
    }

    public static Member newbi(String email, String name){
        return Member.builder()
                .email(email)
                .name(name)
                .income(0)
                .housingType(null)
                .build();
    }
}
