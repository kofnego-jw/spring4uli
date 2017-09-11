package at.ac.uibk.fiba.wang.spring4uli.di;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.stream.Stream;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "at.ac.uibk.fiba.wang.spring4uli.di.service"
})
public class DISpringAppDefinition {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DISpringAppDefinition.class);
        Stream.of(context.getBeanDefinitionNames()).sorted()
                .forEach(System.out::println);
    }
}
