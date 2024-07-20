package sesac.docshelper.domain.docs.dto;

import java.util.List;

public record NaverOcrImageDTO (
        String uid,
        String name,
        String message,
        MatchedTemplate matchedTemplate,
        ValidationResult validationResult,
        Title title,
        List<NaverField> fields
) { }


record MatchedTemplate(
        String id,
        String name
) { }

record ValidationResult (
        String result
) { }

record Title (
        String name,
        Bounding bounding,
        String inferText,
        long inferConfidence
) { }
