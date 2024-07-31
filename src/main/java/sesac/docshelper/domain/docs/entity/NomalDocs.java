package sesac.docshelper.domain.docs.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sesac.docshelper.domain.member.entity.Member;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class NomalDocs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(length = 2048)
    private String url;

    @ManyToOne
    @JsonManagedReference // 어노테이션이 적힌 Entity가 현재 Entity를 관리하는 Entity이다.
    private Member member;

}
