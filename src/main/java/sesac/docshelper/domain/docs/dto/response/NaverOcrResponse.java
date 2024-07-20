package sesac.docshelper.domain.docs.dto.response;

import sesac.docshelper.domain.docs.dto.NaverOcrImageDTO;

import java.util.List;

public record NaverOcrResponse (
        String version,
        String requestId,
        String timeStamp,
        List<NaverOcrImageDTO> images
){ }
