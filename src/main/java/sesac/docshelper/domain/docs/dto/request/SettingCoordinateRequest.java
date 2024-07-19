package sesac.docshelper.domain.docs.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import sesac.docshelper.domain.docs.dto.ItemInfoDTO;

import java.util.List;


public record SettingCoordinateRequest (
        List<ItemInfoDTO> items,
        String title,
        boolean isBlank
) {
}
