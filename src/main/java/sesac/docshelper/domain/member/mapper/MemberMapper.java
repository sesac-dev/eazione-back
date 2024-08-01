package sesac.docshelper.domain.member.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import sesac.docshelper.domain.member.dto.IdentityCardInfoDTO;
import sesac.docshelper.domain.member.dto.MemberInfoDTO;
import sesac.docshelper.domain.member.dto.PassportInfoDTO;
import sesac.docshelper.domain.member.dto.response.AddInfoResponse;
import sesac.docshelper.domain.member.dto.response.IdCardFrontResponse;
import sesac.docshelper.domain.member.entity.IdentityCard;
import sesac.docshelper.domain.member.entity.Member;
import sesac.docshelper.domain.member.entity.Passport;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {

    IdCardFrontResponse entityToFront(IdentityCard identityCard);
    PassportInfoDTO passportToDto(Passport passport);
    IdentityCardInfoDTO idCardToDto(IdentityCard identityCard);

    @Mapping(source = "passport", target = "passportInfo")
    @Mapping(source = "identityCard", target = "identityCardInfo")
    MemberInfoDTO memberToDto(Member member);
    AddInfoResponse memberToAddInfo(Member member);
}
