package at.ac.uibk.fiba.wang.spring4uli.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.stream.Stream;

@SpringBootApplication
@EntityScan(basePackages = {
        "at.ac.uibk.fiba.wang.spring4uli.jpa.ontology"
})
@EnableJpaRepositories(basePackages = {
        "at.ac.uibk.fiba.wang.spring4uli.jpa.repository"
})
@ComponentScan(basePackages = {
        "at.ac.uibk.fiba.wang.spring4uli.jpa.service"
})
public class JpaSpringAppDefinition {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(JpaSpringAppDefinition.class);
        Stream.of(ctx.getBeanDefinitionNames()).sorted()
                .forEach(System.out::println);
    }

}
