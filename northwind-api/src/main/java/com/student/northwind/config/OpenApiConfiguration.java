package com.student.northwind.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class OpenApiConfiguration {
    private final ApplicationProperties appProperties;

    @Bean
    @ConditionalOnMissingBean(name = "apiFirstGroupedOpenAPI")
    public GroupedOpenApi apiFirstGroupedOpenAPI() {
        ApplicationProperties.ApiDocs properties = appProperties.getApiDocs();
        return GroupedOpenApi.builder()
                .group("openapi")
                .addOpenApiCustomiser(openApiCustomiser())
                .packagesToScan("com.student.northwind.controller")
                .build();
    }

    private OpenApiCustomiser openApiCustomiser() {
        return openApi -> {
            Contact contact = (new Contact())
                    .name(appProperties.getApiDocs().getContactName())
                    .url(appProperties.getApiDocs().getContactUrl())
                    .email(appProperties.getApiDocs().getContactEmail());
            openApi.info((new Info())
                    .contact(contact)
                    .title(appProperties.getApiDocs().getTitle())
                    .description(appProperties.getApiDocs().getDescription())
                    .version(appProperties.getApiDocs().getVersion())
                    .termsOfService(appProperties.getApiDocs().getTermsOfServiceUrl())
                    .license((new License())
                            .name(appProperties.getApiDocs().getLicense())
                            .url(appProperties.getApiDocs().getLicenseUrl())));

            var var3 = appProperties.getApiDocs().getServers();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                var server = var3[var5];
                openApi.addServersItem((new io.swagger.v3.oas.models.servers.Server()).url(server.getUrl()).description(server.getDescription()));
            }
        };
    }
}
