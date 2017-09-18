package at.ac.uibk.fiba.wang.spring4uli.rest.web;

import at.ac.uibk.fiba.wang.spring4uli.rest.H2TestBase;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.PersonFW;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.ProjectFull;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.ProjectFullMsg;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.ProjectListMsg;
import at.ac.uibk.fiba.wang.spring4uli.testhelper.TestData;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProjectControllerTest extends H2TestBase {

    @Autowired
    private ProjectController controller;

    @Test
    public void t01_writeData() throws Exception {
        TestData.writeData(personRepo, projectRepo);
    }

    private static Long adamId, bertaId, chrisId, danaId;

    @Test
    public void t02_listProjects() {
        ResponseEntity<ProjectListMsg> resp = controller.listAll();
        Assert.assertEquals(2, resp.getBody().projectList.size());
        adamId = personRepo.findByName("Adam").getId();
        bertaId = personRepo.findByName("Berta").getId();
        chrisId = personRepo.findByName("Chris").getId();
        danaId = personRepo.findByName("Dana").getId();
    }

    private static Long projectId;

    @Test
    public void t03_createNew() {
        PersonFW leader = new PersonFW(adamId, null, null, null);
        List<PersonFW> labors = Arrays.asList(
                new PersonFW(bertaId, null, null,  null),
                new PersonFW(danaId, null, null,  null)
        );
        ProjectFull full = new ProjectFull(null, "Project three", "Destroy the universe",
                leader, labors, Collections.emptyList());
        ResponseEntity<ProjectFullMsg> resp = controller.create(full);
        Assert.assertEquals(HttpStatus.OK, resp.getStatusCode());
        Assert.assertEquals(2, resp.getBody().project.laborators.size());
        Assert.assertTrue(resp.getBody().project.leader.name!=null);
        projectId = resp.getBody().project.id;
//        Assert.assertEquals(Long.valueOf(3L), resp.getBody().project.id);
    }

    @Test
    public void t04_createNewFail() {
        ProjectFull full = new ProjectFull(null, "Project three", "blah", null, null, null);
        ResponseEntity<ProjectFullMsg> resp = controller.create(full);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
    }

    @Test
    public void t05_update() {
        PersonFW leader = new PersonFW(bertaId, null, null, null);
        List<PersonFW> labors = Arrays.asList(
                new PersonFW(chrisId, null, null,  null),
                new PersonFW(danaId, null, null,  null)
        );
        ProjectFull full = new ProjectFull(projectId, "Project three", "Destroy the universe",
                leader, labors, Collections.emptyList());
        ResponseEntity<ProjectFullMsg> resp = controller.update(full, projectId);
        ProjectFull result = resp.getBody().project;
        Assert.assertEquals("Berta", result.leader.name);
        Assert.assertTrue(result.laborators.stream().map(x -> x.name).collect(Collectors.toSet()).contains("Chris"));
    }

    @Test
    public void t06_delete() {
        ResponseEntity<ProjectListMsg> resp = controller.delete(projectId);
        Assert.assertEquals(2, resp.getBody().projectList.size());
    }

    @Test
    public void t99_clearData() throws Exception {
        TestData.clearData(personRepo, projectRepo);
    }


}
