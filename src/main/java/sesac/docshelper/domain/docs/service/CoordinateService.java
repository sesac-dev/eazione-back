package sesac.docshelper.domain.docs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.docshelper.domain.docs.dto.ItemInfoDTO;
import sesac.docshelper.domain.docs.entity.DocsType;
import sesac.docshelper.domain.docs.mapper.DocsMapper;
import sesac.docshelper.domain.docs.repository.CoordinateRepository;
import sesac.docshelper.global.exception.ErrorCode;
import sesac.docshelper.global.exception.GlobalException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoordinateService {

    private final CoordinateRepository coordinateRepository;
    private final DocsMapper docsMapper;

    @Transactional
    public String saveDocumentCoordinate(List<ItemInfoDTO> items, String title, boolean isBlank) {
        try{
            if(coordinateRepository.existsByDocsType(DocsType.valueOf(title))){
                coordinateRepository.deleteAllByDocsType(DocsType.valueOf(title));
            }
            coordinateRepository.saveAll(items.stream()
                    .map(item -> docsMapper.dtoToCoordinate(item,title,isBlank))
                    .toList());
        }catch (IllegalArgumentException e) {
            log.info(e.getMessage());
            throw new GlobalException(ErrorCode.VALID_DOCS_TYPE);
        } catch (Exception e){
            log.info(e.getMessage());
            throw new GlobalException(ErrorCode.BAD_REQUET_TO_DOCS);
        }

        return "좌표들을 성공적으로 저장 하였습니다.";
    }

}
