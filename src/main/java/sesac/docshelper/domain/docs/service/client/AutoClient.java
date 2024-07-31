package sesac.docshelper.domain.docs.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import sesac.docshelper.domain.docs.dto.DocsFillRequest;

@FeignClient(name = "auto-complete-client", url = "https://dev-henry.shop")
public interface AutoClient {

    @PostMapping("/ai/docsfill")
    String getCompleteResponse(@RequestBody DocsFillRequest docsFillRequest);
}
