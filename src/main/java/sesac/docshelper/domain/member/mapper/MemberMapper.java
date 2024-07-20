package sesac.docshelper.domain.member.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sesac.docshelper.domain.member.dto.response.IdCardFrontResponse;
import sesac.docshelper.domain.member.entity.IdentityCard;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {

    IdCardFrontResponse entityToFront(IdentityCard identityCard);
}
