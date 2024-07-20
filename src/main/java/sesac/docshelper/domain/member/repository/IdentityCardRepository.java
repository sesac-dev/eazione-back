package sesac.docshelper.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sesac.docshelper.domain.member.entity.IdentityCard;
import sesac.docshelper.domain.member.entity.Member;

import java.util.Optional;

public interface IdentityCardRepository extends JpaRepository<IdentityCard, Long> {
    boolean existsByMember_Id(Long id);
    Optional<IdentityCard> findByMember_Email(String email);
}
