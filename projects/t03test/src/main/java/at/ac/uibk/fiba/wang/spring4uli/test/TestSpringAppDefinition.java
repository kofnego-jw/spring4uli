package at.ac.uibk.fiba.wang.spring4uli.test;

import at.ac.uibk.fiba.wang.spring4uli.di.DISpringAppDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.stream.Stream;

@SpringBootApplication
@EnableAutoConfiguration
@Import({DISpringAppDefinition.class})
@ComponentScan(basePackages = {
        "at.ac.uibk.fiba.wang.spring4uli.test.service"
})
public class TestSpringAppDefinition {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TestSpringAppDefinition.class);
        Stream.of(context.getBeanDefinitionNames()).sorted()
                .forEach(System.out::println);
    }
}
