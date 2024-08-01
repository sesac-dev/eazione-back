package sesac.docshelper.domain.docs.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sesac.docshelper.domain.docs.dto.DocsFillRequest;

@FeignClient(name = "auto-complete-client", url = "https://dev-henry.shop")
public interface AutoClient {

    @PostMapping( value = "/ai/docsfill", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    MultipartFile getCompleteResponse(@RequestPart MultipartFile image, @RequestPart("data") String data);
}
