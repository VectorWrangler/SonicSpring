package cn.edu.hbnu.sonic.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sonic Music API")
                        .version("1.0")
                        .description("Sonic Music Platform API Documentation")
                        .contact(new Contact()
                                .name("VectorWrangler")
                                .email("garyliu_dev@163.com")
                                .url("https://github.com/VectorWrangler"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}