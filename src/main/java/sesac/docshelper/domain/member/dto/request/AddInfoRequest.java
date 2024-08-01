package sesac.docshelper.domain.member.dto.request;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import sesac.docshelper.domain.member.dto.FamilyMemberDTO;

public record AddInfoRequest(
        long income,
        String phoneNumber,
        List<FamilyMemberDTO> familyMember,
        String housingType,
        String currentWorkplace,
        String currentWorkplaceRegistrationNumber,
        String workplacePhoneNumber
) {
}
