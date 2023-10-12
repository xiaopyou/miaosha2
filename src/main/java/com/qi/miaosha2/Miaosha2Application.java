package com.qi.miaosha2;

import org.mybatis.spring.annotation.MapperScan;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebMvc
@EnableAsync
@EnableScheduling
@EnableRabbit
@EnableSwagger2
@SpringBootApplication
public class Miaosha2Application {

    public static void main(String[] args) {

        SpringApplication.run(Miaosha2Application.class, args);
    }
    @Bean
    public Redisson redisson(){
        Config config=new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        return (Redisson) Redisson.create(config);

    }
    //RestTemplate记得手动加入
    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
