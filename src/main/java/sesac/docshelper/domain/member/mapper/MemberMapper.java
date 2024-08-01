package sesac.docshelper.domain.member.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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
    PassportInfoDTO passportToDto(Passport passport);
    IdentityCardInfoDTO idCardToDto(IdentityCard identityCard);
    @Mapping(source = "passport", target = "passportInfo")
    @Mapping(source = "identityCard", target = "identityCardInfo")
    MemberInfoDTO memberToDto(Member member);
    AddInfoResponse memberToAddInfo(Member member);

    // 기존 엔티티 인스턴스를 DTO로 업데이트 하는 매핑 메서드(passport용)
    void updatePassportFromInfoDto(PassportInfoDTO passportInfoDTO, @MappingTarget Passport passport);
    void updateIdCardFromInfoDto(IdentityCardInfoDTO idCard, @MappingTarget IdentityCard identityCard);
}
