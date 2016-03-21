package uk.gov.defra.jncc.wff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.collect.Lists.*;
import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import static springfox.documentation.schema.AlternateTypeRules.*;
import springfox.documentation.service.ApiInfo;

@SpringBootApplication
@EnableSwagger2
@EntityScan(basePackages = {"uk.gov.defra.jncc.wff.crud.entity"})
@EnableJpaRepositories(basePackages = {"uk.gov.defra.jncc.wff.crud.repository"})
@EnableSpringDataWebSupport
@Profile("default")
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public Docket restAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("uk.gov.defra.jncc.wff.controllers.rest"))
                .paths(PathSelectors.regex("/rest/.*"))
                .build()
                .pathMapping("/")
                .directModelSubstitute(LocalDate.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .enableUrlTemplating(true)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "WIYBY For Farmers API", 
                "API used to build Environmental Reports for legal and recommended actions", 
                "0.0.1", 
                "", 
                "", 
                "OGL", 
                "https://www.nationalarchives.gov.uk/doc/open-government-licence/version/3/");
    }
}
