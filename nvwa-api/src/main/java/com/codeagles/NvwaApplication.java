package com.codeagles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import tk.mybatis.spring.annotation.MapperScan;


/**
 * 启动类
 *
 * @author hcn
 * @create 2020-01-02 13:45
 **/
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@MapperScan(basePackages = "com.codeagles.mapper" )
//扫描所有包以及相关组件包
@ComponentScan(basePackages = {"com.codeagles", "org.n3r.idworker"} )
@EnableScheduling
@EnableRedisHttpSession//开启使用redis-session
public class NvwaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NvwaApplication.class, args);

    }


}
