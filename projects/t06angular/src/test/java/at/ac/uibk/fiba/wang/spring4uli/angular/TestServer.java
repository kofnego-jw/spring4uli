package at.ac.uibk.fiba.wang.spring4uli.angular;

import at.ac.uibk.fiba.wang.spring4uli.jpa.repository.PersonRepo;
import at.ac.uibk.fiba.wang.spring4uli.jpa.repository.ProjectRepo;
import at.ac.uibk.fiba.wang.spring4uli.rest.RestSpringAppDefinition;
import at.ac.uibk.fiba.wang.spring4uli.testhelper.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
        RestSpringAppDefinition.class
})
public class TestServer {

    @Autowired
    PersonRepo personRepo;

    @Autowired
    ProjectRepo projectRepo;

    public void writeTestData() throws Exception {
        TestData.writeData(personRepo, projectRepo);
    }

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext ctx = SpringApplication.run(TestServer.class);
        TestServer testServer = ctx.getBean(TestServer.class);
        testServer.writeTestData();
    }

}
