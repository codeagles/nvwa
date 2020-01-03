package com.codeagles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;


/**
 * 启动类
 *
 * @author hcn
 * @create 2020-01-02 13:45
 **/
@SpringBootApplication
@MapperScan(basePackages = "com.codeagles.mapper" )
public class NvwaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NvwaApplication.class, args);

    }


}
