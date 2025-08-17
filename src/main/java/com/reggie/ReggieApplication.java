package com.reggie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//1380|4G85W1nIQLEFMmzxjQsltMLQnVThllkZqqXz5VJS
@ServletComponentScan
@SpringBootApplication
@EnableTransactionManagement  //开启事务注解
@EnableCaching  //开启springcache的注解功能
public class ReggieApplication {
    //http://localhost:8080/backend/page/login/login.html
    //http://localhost:8080/front/page/login.html
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);
    }

}
