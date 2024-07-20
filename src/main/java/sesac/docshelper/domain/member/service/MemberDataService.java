package sesac.docshelper.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import sesac.docshelper.domain.member.dto.request.AddInfoRequest;
import sesac.docshelper.domain.member.repository.MemberRepository;
import sesac.docshelper.global.exception.ErrorCode;
import sesac.docshelper.global.exception.GlobalException;
import sesac.docshelper.global.util.UserDetailsImpl;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDataService {

    private final MemberRepository memberRepository;

    public String addInfo(UserDetailsImpl userDetails, AddInfoRequest addInfoRequest) {
        try {
            memberRepository.findByEmail(userDetails.getEmail())
                    .map(member -> {member.setIncome(addInfoRequest.income());
                        member.setHousingType(addInfoRequest.housingType());
                        member.setPhoneNumber(addInfoRequest.phoneNumber());
                        return  member;})
                    .map(memberRepository::save);
        } catch (Exception e){
            log.error(e.getMessage());
            throw new GlobalException(ErrorCode.ERROR_IN_ADDINFO);
        }

        return  "성공";
    }

}
