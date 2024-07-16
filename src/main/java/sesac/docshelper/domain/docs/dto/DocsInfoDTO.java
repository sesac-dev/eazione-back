package sesac.docshelper.domain.docs.dto;

import java.util.List;

public record DocsInfoDTO(
        String title, //서류 제목
        List<ItemInfoDTO> items //빈칸 정보
) {
}
