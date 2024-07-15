package sesac.docshelper.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("all-api")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("docs-helper API 문서")
                .version("v0.0.1")
                .description("docs-helper API 명세서.");

        SecurityScheme bearer = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat(HttpHeaders.AUTHORIZATION)
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION);

        // Security 요청 설정
        SecurityRequirement addSecurityItem = new SecurityRequirement();
        addSecurityItem.addList(HttpHeaders.AUTHORIZATION);

        Components components = new Components()
                .addSecuritySchemes(HttpHeaders.AUTHORIZATION, bearer);

        return new OpenAPI()
                .components(components)
                .addSecurityItem(addSecurityItem)
                .addServersItem(new Server().url("https://dev-henry.shop/api")
                        .description("Default Server URL"))
                .addServersItem(new Server().url("http://localhost:8080/api")
                        .description("Local Development Server"))
                .info(info);
    }
}