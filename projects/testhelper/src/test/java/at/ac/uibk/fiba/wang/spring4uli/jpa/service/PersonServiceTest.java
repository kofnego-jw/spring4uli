package at.ac.uibk.fiba.wang.spring4uli.jpa.service;

import at.ac.uibk.fiba.wang.spring4uli.jpa.H2TestBase;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import at.ac.uibk.fiba.wang.spring4uli.testhelper.TestData;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersonServiceTest extends H2TestBase {

    @Autowired
    private PersonService service;

    @Test
    public void t05_createTestData() throws Exception {
        TestData.writeData(personRepo, projectRepo);
    }

    @Test
    public void t06_testTestData() {
        List<Person> all = service.findAll();
        Assert.assertEquals(4, all.size());
    }

    @Test
    public void t07_testPersonFullData() {
        Person dana = service.findOne("Dana");
        PersonFullInfo fullInfo = service.getFullInfo(dana.getId());
        Assert.assertEquals(dana.getId(), fullInfo.person.getId());
        Assert.assertEquals(0, fullInfo.asLaborator.size());
        Assert.assertEquals(1, fullInfo.asLeader.size());

        Person berta = service.findOne("Berta");
        fullInfo = service.getFullInfo(berta.getId());
        Assert.assertEquals(berta.getId(), fullInfo.person.getId());
        Assert.assertEquals(2, fullInfo.asLaborator.size());
        Assert.assertEquals(0, fullInfo.asLeader.size());
    }

    @Test
    public void t08_clearData() throws Exception {
        TestData.clearData(personRepo, projectRepo);
    }


}
