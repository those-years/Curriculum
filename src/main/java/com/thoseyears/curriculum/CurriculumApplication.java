package com.thoseyears.curriculum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.thoseyears.curriculum.dao")//这种方式更灵活
public class CurriculumApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurriculumApplication.class, args);
    }

}
