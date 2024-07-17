package sesac.docshelper.domain.member.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


public record SignUpResponse (
    long id,
    String email,
    String name
) { }
