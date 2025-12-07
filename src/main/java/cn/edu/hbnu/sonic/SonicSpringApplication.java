package cn.edu.hbnu.sonic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"cn.edu.hbnu.sonic"})
public class SonicSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(SonicSpringApplication.class, args);
    }

}