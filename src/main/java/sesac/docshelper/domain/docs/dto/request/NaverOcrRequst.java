package sesac.docshelper.domain.docs.dto.request;

import java.util.List;

public record NaverOcrRequst(
        String version,
        String requestId,
        long timestamp,
        String lang,
        List<NaverImage> images
) {
}


