package com.codeagles;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/3/9
 * Time: 11:21 AM
 * <p>
 * Description: 添加war包的启动类
 */
public class WarStarterApllication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        //指向Springboot启动类
        return builder.sources(NvwaApplication.class);
    }
}
