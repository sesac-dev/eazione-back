package sesac.docshelper.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sesac.docshelper.domain.member.dto.request.AddInfoRequest;
import sesac.docshelper.domain.member.service.MemberDataService;
import sesac.docshelper.global.dto.response.ResultResponse;
import sesac.docshelper.global.util.UserDetailsImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberDataController {
    private final MemberDataService memberDataService;

    @PostMapping("/addInfo")
    public ResponseEntity<ResultResponse<String>> addInfo(@RequestBody AddInfoRequest addInfoRequest, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(ResultResponse.success(memberDataService.addInfo(userDetails, addInfoRequest)));
    }
}