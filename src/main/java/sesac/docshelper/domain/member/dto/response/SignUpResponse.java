package sesac.docshelper.domain.member.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import sesac.docshelper.domain.member.dto.TokenDTO;


public record SignUpResponse (
    long id,
    String email,
    String name,
    boolean isNew,
    TokenDTO tokenDTO
) { }
