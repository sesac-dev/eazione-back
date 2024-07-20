package sesac.docshelper.domain.docs.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sesac.docshelper.domain.docs.dto.request.NaverOcrRequst;
import sesac.docshelper.domain.docs.dto.response.NaverOcrResponse;
import sesac.docshelper.global.config.FeignClientConfig;

@FeignClient(name = "ocr-client", url = "https://f4quzw6a8i.apigw.ntruss.com/custom/v1/32591/439604c44dabd9e417fea356da4fe32d66dda2b9dcf13a9f46d8b707260c9e0a/infer", configuration = FeignClientConfig.class)
public interface OcrClient {

    @PostMapping()
    NaverOcrResponse naverOcrResponse(@RequestBody NaverOcrRequst naverOcrRequst);
}
