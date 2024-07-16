package sesac.docshelper.domain.docs.dto;

public record ItemInfoDTO(
    String columnName, //필요 내용
    double top, //상단 좌표
    double left, //죄측 좌표
    double width, //사이즈
    double height
){
}
