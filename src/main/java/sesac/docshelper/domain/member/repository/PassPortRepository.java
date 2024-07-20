package sesac.docshelper.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sesac.docshelper.domain.member.entity.Passport;

import java.util.Optional;

public interface PassPortRepository extends JpaRepository<Passport, Long> {
    boolean existsByMember_Id(Long id);
    Optional<Passport> findByMember_Email(String email);
}
