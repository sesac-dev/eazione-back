package sesac.docshelper.domain.docs.dto;

import sesac.docshelper.domain.member.dto.MemberInfoDTO;

public record DocsFillRequest( //문서 자동완성 요청 파라미터
        MemberInfoDTO memberInfo,
        DocsInfoDTO docsInfoDTO // 서류 정보
) {
}
