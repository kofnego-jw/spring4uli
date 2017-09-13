package at.ac.uibk.fiba.wang.spring4uli.rest;

import at.ac.uibk.fiba.wang.spring4uli.jpa.JpaSpringAppDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.stream.Stream;

@SpringBootApplication
@Import(JpaSpringAppDefinition.class)
@ComponentScan("at.ac.uibk.fiba.wang.spring4uli.rest.web")
public class RestSpringAppDefinition {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(RestSpringAppDefinition.class);
        Stream.of(ctx.getBeanDefinitionNames()).sorted()
                .forEach(System.out::println);
    }

}
