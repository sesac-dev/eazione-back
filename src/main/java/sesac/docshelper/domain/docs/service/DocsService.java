package sesac.docshelper.domain.docs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sesac.docshelper.domain.docs.dto.response.GetMyDocsListResponse;
import sesac.docshelper.domain.docs.dto.response.StoreNomalDocsResponse;
import sesac.docshelper.domain.docs.entity.NomalDocs;
import sesac.docshelper.domain.docs.repository.NomalDocsRepository;
import sesac.docshelper.domain.member.service.S3MultiPartUploader;
import sesac.docshelper.global.exception.ErrorCode;
import sesac.docshelper.global.exception.GlobalException;
import sesac.docshelper.global.util.UserDetailsImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocsService {

    private final S3MultiPartUploader s3MultiPartUploader;
    private final NomalDocsRepository nomalDocsRepository;

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
                .map(nomalDocs -> urls.add(nomalDocs.getUrl()))
                .orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_DOCS));
        return new GetMyDocsListResponse(urls);
    }

    public String getAutoTranslate(UserDetailsImpl userDetails, MultipartFile file, String nation) {
        try {
           // 1. 서류 이미지로 만들기
           String url = s3MultiPartUploader.upload(file);

           return null;
        }catch (Exception e) {
            log.info(e.getMessage());
            throw new GlobalException(ErrorCode.CANT_UPLOAD_FILE);
        }
    }

}
