package at.ac.uibk.fiba.wang.spring4uli.rest.web;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import at.ac.uibk.fiba.wang.spring4uli.rest.H2TestBase;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.PersonFullMsg;
import at.ac.uibk.fiba.wang.spring4uli.testhelper.TestData;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersonControllerTest extends H2TestBase {

    @Autowired
    private PersonController personController;

    @Test
    public void t01_writeTestData() throws Exception {
        TestData.writeData(personRepo, projectRepo);
    }

    @Test
    public void t02_readOne() {
        Person adam = personRepo.findByName("Adam");
        ResponseEntity<PersonFullMsg> re = personController.findOne(adam.getId());
        Assert.assertEquals(HttpStatus.OK, re.getStatusCode());
        PersonFullMsg msg = re.getBody();
        Assert.assertEquals(1, msg.person.asLeader.size());
        Assert.assertEquals(1, msg.person.asLaborator.size());
    }

    @Test
    public void t99_clearData() throws Exception {
        TestData.clearData(personRepo, projectRepo);
    }

}
