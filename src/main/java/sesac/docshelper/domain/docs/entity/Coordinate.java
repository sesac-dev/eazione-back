package sesac.docshelper.domain.docs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Coordinate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private DocsType docsType;  // 어떤 서류에 대한 좌표인지
    private boolean isBlank;    // 빈칸 좌표인지 질문의 좌표인지
    // 실제 값
    private String columnName;  // 컬럼명
    private double top;         // 상단 좌표
    @Column(name = "`left`")    // 예약어네...
    private double left;        // 좌측 좌표 -> top과 합치면 좌상단
    private double width;       // 넓이
    private double height;      // 높이
    @ColumnDefault("false")
    private boolean isCheck;      // check를 위한 공란인가 아닌가
}
