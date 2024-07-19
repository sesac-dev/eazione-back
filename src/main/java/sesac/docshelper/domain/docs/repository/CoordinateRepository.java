package sesac.docshelper.domain.docs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sesac.docshelper.domain.docs.entity.Coordinate;
import sesac.docshelper.domain.docs.entity.DocsType;


public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {

    boolean existsByDocsType(DocsType docsType);
    void deleteAllByDocsType(DocsType docsType);

}
