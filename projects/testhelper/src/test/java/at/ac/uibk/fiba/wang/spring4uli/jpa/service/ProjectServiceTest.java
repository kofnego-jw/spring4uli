package at.ac.uibk.fiba.wang.spring4uli.jpa.service;

import at.ac.uibk.fiba.wang.spring4uli.jpa.H2TestBase;
import at.ac.uibk.fiba.wang.spring4uli.jpa.MyJpaException;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;
import at.ac.uibk.fiba.wang.spring4uli.jpa.repository.PersonRepo;
import at.ac.uibk.fiba.wang.spring4uli.jpa.repository.PictureRepo;
import at.ac.uibk.fiba.wang.spring4uli.jpa.repository.ProjectRepo;
import at.ac.uibk.fiba.wang.spring4uli.testhelper.TestData;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProjectServiceTest extends H2TestBase {

    @Autowired
    private ProjectService service;

    @Test
    public void t01_createTestData() throws Exception {
        TestData.writeData(personRepo, projectRepo);
    }

    @Test(expected = MyJpaException.class)
    public void t02_createProjectWithSamename() throws Exception {
        Project p = new Project();
        p.setName("Project one");
        service.saveOrUpdate(p);
    }

    @Test
    public void t99_clearData() throws Exception {
        TestData.clearData(personRepo, projectRepo);
    }

}
