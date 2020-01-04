package com.codeagles.config;

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

/**
 * API文档配置类
 *
 * @author hcn
 * @create 2020-01-04 15:13
 **/
@Configuration
@EnableSwagger2
public class Swagger2 {

    //http://localhost:8088/swagger-ui.html

    //配置swagger2核心配置 docket
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)//指定Api类型为swagger2
                .apiInfo(apiInfo())//用于定义api文档汇总信息
                .select().apis(RequestHandlerSelectors.basePackage("com.codeagles.controller"))//扫描controller
                .paths(PathSelectors.any())//所有的controller
                .build();
    }


    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("女娲项目Api接口")//文档页标题
                .contact(new Contact("codeagles","http://codeagles.com","hcn0521@outlook.com"))
                .description("回炉重造项目")
                .version("1.0.1") //文档版本号
                .termsOfServiceUrl("http://codeagles.com")
                .build();

         }
}
