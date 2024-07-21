package sesac.docshelper.domain.docs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sesac.docshelper.domain.docs.entity.NomalDocs;

import java.util.Optional;

public interface NomalDocsRepository extends JpaRepository<NomalDocs, Long> {

    Optional<NomalDocs> findByMember_Id(long id);
    Optional<NomalDocs> findAllByMember_Id(long id);
}
