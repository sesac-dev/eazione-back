package sesac.docshelper.domain.docs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sesac.docshelper.domain.docs.dto.ItemInfoDTO;
import sesac.docshelper.domain.docs.dto.request.SettingCoordinateRequest;
import sesac.docshelper.domain.docs.service.CoordinateService;
import sesac.docshelper.global.dto.response.ResultResponse;

import java.util.List;

@RestController
@RequestMapping("/docs")
@RequiredArgsConstructor
public class DocsController {

    private final CoordinateService coordinateService;

    @PostMapping
    public ResponseEntity<ResultResponse<String>> setCoordinates4Docs(@RequestBody SettingCoordinateRequest request) {
        return ResponseEntity.ok(ResultResponse.success(coordinateService.saveDocumentCoordinate(request.items(), request.title(), request.isBlank()))); // 성공이라는 200 code와 함께 매개 변수로 받은 값을 전달
    }
}
