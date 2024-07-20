package sesac.docshelper.domain.docs.dto;
import sesac.docshelper.domain.docs.dto.Bounding;

public record NaverField (
        String name,
        Bounding bounding,
        String valueType,
        String inferText,
        long inferConfidenct
) { }