package com.codeagles.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/2/9
 * Time: 9:17 PM
 * <p>
 * Description:
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.build();
    }

    //实现静态资源注册
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")//映射swagger2
                .addResourceLocations("file:/Users/codeagles/IdeaProjects/new/images/");//映射本地静态资源

    }
}
