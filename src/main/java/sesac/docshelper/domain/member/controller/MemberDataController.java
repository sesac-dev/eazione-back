package sesac.docshelper.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sesac.docshelper.domain.member.dto.IdentityCardInfoDTO;
import sesac.docshelper.domain.member.dto.PassportInfoDTO;
import sesac.docshelper.domain.member.dto.request.AddInfoRequest;
import sesac.docshelper.domain.member.dto.response.AddInfoResponse;
import sesac.docshelper.domain.member.dto.response.IdCardBackResponse;
import sesac.docshelper.domain.member.dto.response.IdCardFrontResponse;
import sesac.docshelper.domain.member.entity.Member;
import sesac.docshelper.domain.member.service.MemberDataService;
import sesac.docshelper.global.dto.response.ResultResponse;
import sesac.docshelper.global.util.UserDetailsImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberDataController {
    private final MemberDataService memberDataService;

    @Operation(description = "사용자 추가 정보 입력하기 ")
    @PostMapping("/add-info")
    public ResponseEntity<ResultResponse<AddInfoResponse>> addInfo(@RequestPart(value = "info") AddInfoRequest addInfoRequest, @RequestPart(value = "img") MultipartFile img, @RequestPart(value = "signature") MultipartFile sign ,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(ResultResponse.success(memberDataService.addInfo(
            userDetails, addInfoRequest, img, sign )));
    }

    @Operation(description = "사용자 추가 정보 가져오기")
    @GetMapping("/add-info")
    public ResponseEntity<ResultResponse<AddInfoResponse>> getInfo(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(ResultResponse.success(memberDataService.getInfo(userDetails)));
    }

    @Operation(description = "외국인 등록증 or 여권 읽기, 여권이면 passport, 외국인 등록증 앞: foreginerfront, 뒤: foreginerback")
    @PostMapping("/add-ocr/{docsType}")
    public ResponseEntity<ResultResponse<Object>> addIdCardFront(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "img")MultipartFile img,
            @PathVariable(value = "docsType") String docsType
            ){
        return ResponseEntity.ok(ResultResponse.success(memberDataService.addOcrInfo(userDetails,img,docsType)));
    }

    @Operation(description = "외국인 등록증 or 여권 가져오기 -> docsType: passport, IdCard")
    @GetMapping("/add-ocr/{docsType}")
    public ResponseEntity<ResultResponse<Object>> getOcrDocs(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable(value = "docsType") String docsType
    ){
        return ResponseEntity.ok(ResultResponse.success(memberDataService.getOcrInfo(userDetails,docsType)));
    }

    @Operation(description = "여권 안 들어온 정보 업데이트, DTO 같음")
    @PatchMapping("add-ocr/passport")
    public  ResponseEntity<ResultResponse<String>> addMore(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody  PassportInfoDTO passportInfoDTO
    ){
        return  ResponseEntity.ok(ResultResponse.success(memberDataService.updatePassport(userDetails, passportInfoDTO)));
    }

    @Operation(description = "외국인 등록증 안 들어온 데이터, DTO 같음")
    @PatchMapping("add-ocr/idCard")
    public ResponseEntity<ResultResponse<String>> addMore(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody IdentityCardInfoDTO identityCardInfo
    ){
        return ResponseEntity.ok(ResultResponse.success(memberDataService.updateIdcard(userDetails, identityCardInfo)));
    }

}
