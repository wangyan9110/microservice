package info.yywang.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author shengguo
 * @version 1.0
 * @date 2017-03-07 23:24
 *
 * SpringBootApplication核心注解
 * @Configuration @EnableAutoConfiguration
 * @ComponentScan 组合
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
