package sesac.docshelper.domain.docs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sesac.docshelper.domain.docs.entity.Coordinate;
import sesac.docshelper.domain.docs.entity.DocsType;

import java.util.List;


public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {

    boolean existsByDocsTypeAndBlank(DocsType docsType, boolean isBlank);
    void deleteAllByDocsTypeAndBlank(DocsType docsType, boolean isBlank);
    List<Coordinate> findAllByDocsTypeAndBlank(DocsType docsType, boolean isBlank);

}
