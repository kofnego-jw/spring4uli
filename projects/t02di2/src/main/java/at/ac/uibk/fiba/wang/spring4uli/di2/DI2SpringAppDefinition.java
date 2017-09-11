package at.ac.uibk.fiba.wang.spring4uli.di2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.util.stream.Stream;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "at.ac.uibk.fiba.wang.spring4uli.di2.service"
})
public class DI2SpringAppDefinition {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DI2SpringAppDefinition.class);
        Stream.of(context.getBeanDefinitionNames()).sorted()
                .forEach(System.out::println);
    }


    @Bean
    public File repoDir() {
        return new File(tempDir(), "repo");
    }

    @Bean
    public File tempDir() {
        return new File("temp");
    }

}
