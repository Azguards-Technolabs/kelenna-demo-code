package com.azguards.app.confirguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.azguards.app.util.IConstant;

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
@EnableAutoConfiguration
public class SwaggerConfig extends WebMvcConfigurationSupport{
    
     @Bean
        public Docket productApi() {
            return new Docket(DocumentationType.SWAGGER_2)
            		.groupName("version1")
                    .select()
                    .apis(RequestHandlerSelectors.basePackage(IConstant.BASE_PACKAGE))
                    .paths(PathSelectors.regex("/api/v1.*"))
                    .build()
                    .apiInfo(metaData());
        }
        private ApiInfo metaData() {
            return new ApiInfoBuilder()
                    .title(IConstant.INSTITUDE_API)
                    .description("")
                    .version("1.0.0")
                    .license("Apache License Version 2.0")
                    .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
                    .contact(new Contact("", "",""))
                    .build();
        }
        @Override
        protected void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("swagger-ui.html")
                    .addResourceLocations("classpath:/META-INF/resources/");
     
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
}
