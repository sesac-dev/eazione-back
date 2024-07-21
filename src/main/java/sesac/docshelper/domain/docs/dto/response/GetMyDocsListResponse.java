package sesac.docshelper.domain.docs.dto.response;

import java.util.List;

public record GetMyDocsListResponse (
        List<String> urls
) {
}
