package at.ac.uibk.fiba.wang.spring4uli.rest.web;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import at.ac.uibk.fiba.wang.spring4uli.rest.H2TestBase;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.PersonFW;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.PersonFullMsg;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.PersonListMsg;
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

    private static Long personId;

    @Test
    public void t03_createNew() {
        PersonFW fw = new PersonFW(null, "Evi", "evi@example.com", "info");
        ResponseEntity<PersonFullMsg> resp = personController.create(fw);
        Assert.assertEquals(HttpStatus.OK, resp.getStatusCode());
        personId = resp.getBody().person.id;
    }

    @Test
    public void t04_update() {
        PersonFW fw = new PersonFW(personId, "Evi", "evi@example.com", "new info");
        ResponseEntity<PersonFullMsg> resp = personController.update(fw, personId);
        Assert.assertEquals("new info", resp.getBody().person.info);
    }

    @Test
    public void t05_createNewButFail() {
        PersonFW fw = new PersonFW(null, "Evi", "evi2@example.com", "info evi2");
        ResponseEntity<PersonFullMsg> resp = personController.create(fw);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
    }

    @Test
    public void t06_updateWithInconsistentId() {
        PersonFW fw = new PersonFW(personId, "Evi", "evi3@example.com", "info evi3");
        ResponseEntity<PersonFullMsg> resp = personController.update(fw, 2L);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
    }

    @Test
    public void t07_delete() {
        ResponseEntity<PersonListMsg> resp = personController.delete(personId);
        Assert.assertEquals(HttpStatus.OK, resp.getStatusCode());
        Assert.assertEquals(4, resp.getBody().personList.size());
    }

    @Test
    public void t08_deleteFail() {
        ResponseEntity<PersonListMsg> resp = personController.delete(1L);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
    }


    @Test
    public void t99_clearData() throws Exception {
        TestData.clearData(personRepo, projectRepo);
    }

}
