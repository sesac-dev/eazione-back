package sesac.docshelper.domain.docs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sesac.docshelper.domain.docs.dto.DocsFillRequest;
import sesac.docshelper.domain.docs.dto.DocsInfoDTO;
import sesac.docshelper.domain.docs.dto.ItemInfoDTO;
import sesac.docshelper.domain.docs.dto.response.DocsCountResponse;
import sesac.docshelper.domain.docs.dto.response.GetMyDocsListResponse;
import sesac.docshelper.domain.docs.dto.response.StoreNomalDocsResponse;
import sesac.docshelper.domain.docs.entity.DocsType;
import sesac.docshelper.domain.docs.entity.NomalDocs;
import sesac.docshelper.domain.docs.mapper.DocsMapper;
import sesac.docshelper.domain.docs.repository.CoordinateRepository;
import sesac.docshelper.domain.docs.repository.NomalDocsRepository;
import sesac.docshelper.domain.docs.service.client.AutoClient;
import sesac.docshelper.domain.member.dto.IdentityCardInfoDTO;
import sesac.docshelper.domain.member.dto.MemberInfoDTO;
import sesac.docshelper.domain.member.dto.PassportInfoDTO;
import sesac.docshelper.domain.member.mapper.MemberMapper;
import sesac.docshelper.domain.member.repository.IdentityCardRepository;
import sesac.docshelper.domain.member.repository.PassPortRepository;
import sesac.docshelper.domain.member.service.S3MultiPartUploader;
import sesac.docshelper.global.exception.ErrorCode;
import sesac.docshelper.global.exception.GlobalException;
import sesac.docshelper.global.util.UserDetailsImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocsService {

    private final AutoClient autoClient;
    private final S3MultiPartUploader s3MultiPartUploader;
    private final MemberMapper memberMapper;
    private final DocsMapper docsMapper;
    private final NomalDocsRepository nomalDocsRepository;
    private final CoordinateRepository coordinateRepository;
    private final PassPortRepository passPortRepository;
    private final IdentityCardRepository identityCardRepository;
    private final ObjectMapper objectMapper;

    public StoreNomalDocsResponse storeNomalDocs(MultipartFile file, UserDetailsImpl userDetails) {
        try {
            String url = s3MultiPartUploader.upload(file);
            NomalDocs newDocs = new NomalDocs();
            newDocs.setName(userDetails.getEmail()+ UUID.randomUUID());
            newDocs.setMember(userDetails.getMember());
            newDocs.setUrl(url);
            nomalDocsRepository.save(newDocs);
            return new StoreNomalDocsResponse(url);
        } catch (IOException e) {
            throw new GlobalException(ErrorCode.CANT_UPLOAD_FILE);
        }
    }

    public GetMyDocsListResponse getMyDocs(UserDetailsImpl userDetails) {
        List<String> urls = new ArrayList<>();
        nomalDocsRepository.findAllByMember_Id(userDetails.getMember().getId())
                .orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_DOCS))
            .forEach(nomalDocs -> urls.add(nomalDocs.getUrl()));
        return new GetMyDocsListResponse(urls);
    }

    public String getAutoTranslate(UserDetailsImpl userDetails, MultipartFile file, String nation) {
        try {
           // 1. 서류 이미지로 만들기
           String title = "외국인 통합 신청서";
           //String url = s3MultiPartUploader.upload(file);
           List<ItemInfoDTO> items =  coordinateRepository.findAllByDocsTypeAndBlank(DocsType.TEST_DOCS, false).stream()
               .map(docsMapper::entityToDto)
               .toList();
           List<ItemInfoDTO> emptyItems = coordinateRepository.findAllByDocsTypeAndBlank(DocsType.TEST_DOCS, true).stream()
               .map(docsMapper::entityToDto)
               .toList();
           DocsFillRequest docsFillRequest = new DocsFillRequest(memberMapper.memberToDto(userDetails.getMember()),new DocsInfoDTO(title, nation, items, emptyItems));
           logDocsFillRequest(docsFillRequest);
            String jsonData = objectMapper.writeValueAsString(docsFillRequest);
            Response response = autoClient.getCompleteResponse(file, jsonData);
            // 응답을 byte[]로 변환
            InputStream inputStream = response.body().asInputStream();
            byte[] bytes = inputStream.readAllBytes();

            return s3MultiPartUploader.upload(bytes);
        }catch (Exception e) {
            log.info(e.getMessage());
            throw new GlobalException(ErrorCode.CANT_UPLOAD_FILE);
        }
    }

    public DocsCountResponse getCount(UserDetailsImpl userDetails){
        return new DocsCountResponse(
            passPortRepository.existsByMember_Id(userDetails.getMember().getId()),
            identityCardRepository.existsByMember_Id(userDetails.getMember().getId()),
            nomalDocsRepository.countAllByMember_Id(userDetails.getMember().getId())
        );
    }


    public void logDocsFillRequest(DocsFillRequest request) {
        if (request == null) {
            log.info("DocsFillRequest is null");
            return;
        }

        log.info("DocsFillRequest:");
        logMemberInfo(request.memberInfo());
        logDocsInfo(request.docsInfoDTO());
    }

    private void logMemberInfo(MemberInfoDTO memberInfo) {
        if (memberInfo == null) {
            log.info("      MemberInfoDTO is null");
            return;
        }

        log.info("  MemberInfoDTO: email={}, name={}, profileImage={}, passportInfo={}, identityCardInfo={}, income={}, housingType={}",
            memberInfo.email(), memberInfo.name(), memberInfo.profileImage(),
            memberInfo.passportInfo(), memberInfo.identityCardInfo(), memberInfo.income(), memberInfo.housingType());
    }

    private void logPassportInfo(PassportInfoDTO passportInfo) {
        if (passportInfo == null) {
            log.info("    PassportInfoDTO is null");
            return;
        }

        log.info("    PassportInfoDTO: {}",
            passportInfo); // 여기에 passportInfo 필드를 한 줄로 로그 출력
    }

    private void logIdentityCardInfo(IdentityCardInfoDTO identityCardInfo) {
        if (identityCardInfo == null) {
            log.info("IdentityCardInfoDTO is null");
            return;
        }

        log.info("    IdentityCardInfoDTO: {}",
            identityCardInfo); // 여기에 identityCardInfo 필드를 한 줄로 로그 출력
    }

    private void logDocsInfo(DocsInfoDTO docsInfo) {
        if (docsInfo == null) {
            log.info("  DocsInfoDTO is null");
            return;
        }

        log.info("  DocsInfoDTO: title={}, translate={}",
            docsInfo.title(), docsInfo.translate());

        logItems("Items", docsInfo.items());
        logItems("EmptyItems", docsInfo.emptyItems());
    }

    private void logItems(String label, List<ItemInfoDTO> items) {
        if (items == null || items.isEmpty()) {
            log.info("    {} are null or empty", label);
            return;
        }

        log.info("    {}:", label);
        for (ItemInfoDTO item : items) {
            log.info("      ItemInfoDTO: columnName={}, top={}, left={}, width={}, height={}, check={}, photo={}",
                item.columnName(), item.top(), item.left(), item.width(), item.height(), item.check(), item.photo());
        }
    }
}
