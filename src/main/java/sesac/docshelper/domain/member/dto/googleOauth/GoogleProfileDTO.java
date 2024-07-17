package sesac.docshelper.domain.member.dto.googleOauth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public record GoogleProfileDTO (
        String id,
        String name,
        String email,
        String picture
) { }
