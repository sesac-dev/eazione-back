package sesac.docshelper.domain.docs.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record DocsInfoDTO(
        String title, //서류 제목
        MultipartFile image, //원본 서류 이미지
        List<ItemInfoDTO> items //빈칸 정보
) {
}
