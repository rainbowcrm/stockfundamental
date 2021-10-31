package com.primus.main;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class ReadContent extends SpringApplication {


    public static void main(String[] args) {
        SpringApplication.run(ReadContent.class,args);
    }
}
