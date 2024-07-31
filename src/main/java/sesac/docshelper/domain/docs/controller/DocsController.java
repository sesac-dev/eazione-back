package sesac.docshelper.domain.docs.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sesac.docshelper.domain.docs.dto.ItemInfoDTO;
import sesac.docshelper.domain.docs.dto.request.SettingCoordinateRequest;
import sesac.docshelper.domain.docs.dto.response.GetMyDocsListResponse;
import sesac.docshelper.domain.docs.dto.response.StoreNomalDocsResponse;
import sesac.docshelper.domain.docs.service.CoordinateService;
import sesac.docshelper.domain.docs.service.DocsService;
import sesac.docshelper.global.dto.response.ResultResponse;
import sesac.docshelper.global.util.UserDetailsImpl;

import javax.xml.transform.Result;
import java.util.List;

@RestController
@RequestMapping("/docs")
@RequiredArgsConstructor
public class DocsController {

    private final CoordinateService coordinateService;
    private final DocsService docsService;

    @Operation(description = "OCR 하고 싶은 서류의 좌표 저장")
    @PostMapping
    public ResponseEntity<ResultResponse<String>> setCoordinates4Docs(@RequestBody SettingCoordinateRequest request) {
        return ResponseEntity.ok(ResultResponse.success(coordinateService.saveDocumentCoordinate(request.items(), request.title(), request.isBlank()))); // 성공이라는 200 code와 함께 매개 변수로 받은 값을 전달
    }

    @Operation(description = "일반 서류 저장")
    @PostMapping("/nomal")
    public ResponseEntity<ResultResponse<StoreNomalDocsResponse>> storeNomalDocs(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart(name = "img")MultipartFile file) {
        return ResponseEntity.ok(ResultResponse.success(docsService.storeNomalDocs(file,userDetails)));
    }

    @Operation(description = "사용자의 모든 서류 가져오기")
    @GetMapping("/nomal")
    public ResponseEntity<ResultResponse<GetMyDocsListResponse>> getDocsList(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(ResultResponse.success(docsService.getMyDocs(userDetails)));
    }

    @Operation(description = "서류 자동번역 & 자동 완성")
    @PostMapping("/auto/translation/{nation}")
    public ResponseEntity<ResultResponse<String>> getAutoTranslate(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @RequestPart(name = "img") MultipartFile img,
                                                             @PathVariable(name = "nation") String nation){
        return ResponseEntity.ok(ResultResponse.success(docsService.getAutoTranslate(userDetails,img,nation)));
    }
}
