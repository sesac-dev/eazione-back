package sesac.docshelper.domain.docs.mapper;

import org.mapstruct.*;
import sesac.docshelper.domain.docs.dto.ItemInfoDTO;
import sesac.docshelper.domain.docs.entity.Coordinate;
import sesac.docshelper.domain.docs.entity.DocsType;

import java.util.List;
import java.util.StringTokenizer;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DocsMapper {
    @Named("dtoToEntity")
    @Mapping(target = "docsType", expression = "java(sesac.docshelper.domain.docs.entity.DocsType.valueOf(title))")
    @Mapping(target = "blank", source = "isBlank")
    @Mapping(target = "columnName", source = "item.columnName")
    @Mapping(target = "top", source = "item.top")
    @Mapping(target = "left", source = "item.left")
    @Mapping(target = "width", source = "item.width")
    @Mapping(target = "height", source = "item.height")
    @Mapping(target = "check", ignore = true) // check 필드를 이후에 설정
    Coordinate dtoToCoordinate (ItemInfoDTO item, String title, boolean isBlank);

    @AfterMapping
    default void setCheck(@MappingTarget Coordinate coordinate, ItemInfoDTO item) {
        StringTokenizer st = new StringTokenizer(item.columnName());
        String lastToken = "";
        while (st.hasMoreTokens()){
            lastToken = st.nextToken();
        }
        coordinate.setCheck("check".equalsIgnoreCase(lastToken));
    }
}

