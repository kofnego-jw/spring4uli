package at.ac.uibk.fiba.wang.spring4uli.angular;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;
import at.ac.uibk.fiba.wang.spring4uli.jpa.repository.PersonRepo;
import at.ac.uibk.fiba.wang.spring4uli.jpa.repository.ProjectRepo;
import at.ac.uibk.fiba.wang.spring4uli.jpa.service.PictureService;
import at.ac.uibk.fiba.wang.spring4uli.rest.RestSpringAppDefinition;
import at.ac.uibk.fiba.wang.spring4uli.testhelper.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
@Import({
        RestSpringAppDefinition.class
})
public class TestServer {

    @Autowired
    PersonRepo personRepo;

    @Autowired
    ProjectRepo projectRepo;

    @Autowired
    PictureService pictureService;

    public void writeTestData() throws Exception {
        TestData.writeData(personRepo, projectRepo);
        Set<Person> persons = Stream.of(personRepo.findByName("Berta"), personRepo.findByName("Dana"))
                .collect(Collectors.toSet());
        Set<Project> projects = Stream.of(projectRepo.findByName("Project two")).collect(Collectors.toSet());
        pictureService.save("sample.jpg", new File("projects/testhelper/src/test/resources/sampleimages/sample.jpg"),
                persons, projects, false);
    }

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext ctx = SpringApplication.run(TestServer.class);
        TestServer testServer = ctx.getBean(TestServer.class);
        testServer.writeTestData();
    }

}
