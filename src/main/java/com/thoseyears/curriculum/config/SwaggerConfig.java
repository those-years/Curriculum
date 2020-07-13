package com.thoseyears.curriculum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    //http://localhost:8080/swagger-ui.html
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                //扫描包
                .apis(RequestHandlerSelectors.basePackage("com.thoseyears.curriculum.controller"))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                        .title("报名子系统接口文档")
                        .description("SpringBoot整合Swagger，自动整合接口文档")
                        .version("9.0")
                        .contact(new Contact("thoseyears","www.thoseyears.com","1392178770@qq.com"))
                        .license("The thoseyears License")
                        .licenseUrl("http://www.thoseyears.com")
                        .build());
    }
}