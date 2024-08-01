package sesac.docshelper.domain.docs.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import sesac.docshelper.domain.docs.dto.DocsFillRequest;
import org.springframework.http.ResponseEntity;

@FeignClient(name = "auto-complete-client", url = "https://dev-henry.shop")
public interface AutoClient {

    @PostMapping( value = "/ai/docsfill", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    MultipartFile getCompleteResponse(@RequestPart("file") MultipartFile image, @RequestPart("data") DocsFillRequest data);

}
