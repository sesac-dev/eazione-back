package sesac.docshelper.domain.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String email;
    String name;

    //추가 정보
    Long passportId; //여권 id
    Long identityCardId; //외국인등록증 id
    Long income; //소득
    String housingType; //주거형태
}
