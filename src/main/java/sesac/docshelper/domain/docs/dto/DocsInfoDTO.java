package sesac.docshelper.domain.docs.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record DocsInfoDTO(
        String title, //서류 제목
        String image, //원본 서류 이미지
        String translate, // 번역에 쓸 언어
        List<ItemInfoDTO> items, //내용 있는 칸 좌표
        List<ItemInfoDTO> emptyItems // 빈칸 좌표
) {
}
