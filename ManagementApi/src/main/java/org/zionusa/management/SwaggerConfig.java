package org.zionusa.management;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${microsoft.o365.domain}")
    private String microsoftO365Domain;
    @Value("${microsoft.tenant.name}")
    private String microsoftTenantName;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build().apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo() {
        final String contactName = microsoftTenantName + " App Support";
        final String contactUrl = "my." + microsoftO365Domain;
        final String contactEmail = "AppSupport@" + microsoftO365Domain;
        return new ApiInfoBuilder().title("Spring Boot REST API")
            .description("User Management REST API")
            .contact(new Contact(contactName, contactUrl, contactEmail))
            .license("Apache 2.0")
            .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0.html")
            .version("1.0.0")
            .build();
    }
}
