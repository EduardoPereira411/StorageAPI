package com.spring.storage.Configuration;

import com.spring.storage.FileManagement.FileStorageProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
@EnableConfigurationProperties({ FileStorageProperties.class })
public class ApiConfig {

    @Bean
    public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }


    //OpenAPI
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(new Info().title("Storage API").description("Api that will control the Storage of a given Type of data").version("v1.0")
                .contact(new Contact().name("Eduardo Pereira").email("eduardocastropereira@gmail.com")).termsOfService("Non-specified as of writing the code"));
    }
}
