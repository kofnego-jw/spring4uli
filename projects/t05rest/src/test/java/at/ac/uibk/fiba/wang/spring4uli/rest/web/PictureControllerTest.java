package at.ac.uibk.fiba.wang.spring4uli.rest.web;

import at.ac.uibk.fiba.wang.spring4uli.rest.H2TestBase;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.*;
import at.ac.uibk.fiba.wang.spring4uli.testhelper.TestData;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PictureControllerTest extends H2TestBase {

    private static final File SAMPLE_JPEG = new File("../testhelper/src/test/resources/sampleimages/sample.jpg");
    private static final File SAMPLE_PNG = new File("../testhelper/src/test/resources/sampleimages/sample.png");

    @Autowired
    private PictureController controller;

    @Test
    public void t00_createDate() throws Exception {
        TestData.writeData(personRepo, projectRepo);
    }

    private static Long adamId, bertaId, chrisId, danaId, project1Id, project2Id;

    @Test
    public void t00a_populateIds() {
        adamId = personRepo.findByName("Adam").getId();
        bertaId = personRepo.findByName("Berta").getId();
        chrisId = personRepo.findByName("Chris").getId();
        danaId = personRepo.findByName("Dana").getId();
        project1Id = projectRepo.findByName("Project one").getId();
        project2Id = projectRepo.findByName("Project two").getId();
    }

    private static Long pictId;

    @Test
    public void t01_createPicture() throws Exception {
        MockMultipartFile file = new MockMultipartFile(SAMPLE_JPEG.getName(), SAMPLE_JPEG.getName(), "image/jpeg",
                FileUtils.readFileToByteArray(SAMPLE_JPEG));
        ResponseEntity<PictureFullMsg> resp = controller.create(file, "" + adamId + "," + bertaId, "" + project2Id);
//        System.out.println(resp.getBody().message);
        Assert.assertEquals(HttpStatus.OK, resp.getStatusCode());
        PictureFull p = resp.getBody().picture;
        pictId = p.id;
        Assert.assertEquals(1, p.projects.size());
        Assert.assertEquals(Long.valueOf(project2Id), p.projects.get(0).id);
        Assert.assertEquals(2, p.persons.size());
    }

    @Test
    public void t01a_findPicts() {
        ResponseEntity<PictureListMsg> resp = controller.findByPerson(adamId);
        Assert.assertEquals(1, resp.getBody().pictureList.size());
        resp = controller.findByProject(project2Id);
        Assert.assertEquals(1, resp.getBody().pictureList.size());
    }

    @Test
    public void t02_updatePicture() throws Exception {
        MockMultipartFile file = new MockMultipartFile(SAMPLE_PNG.getName(), SAMPLE_PNG.getName(), "image/png",
                FileUtils.readFileToByteArray(SAMPLE_PNG));

        ResponseEntity<PictureFullMsg> resp = controller.update(file, "" + bertaId + "," + chrisId, "" + project1Id, pictId);
        Assert.assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    public void t02a_findPictures() throws Exception {
        ResponseEntity<PictureListMsg> resp = controller.findByPerson(adamId);
        Assert.assertEquals(0, resp.getBody().pictureList.size());
        resp = controller.findByProject(project2Id);
        Assert.assertEquals(0, resp.getBody().pictureList.size());
        resp = controller.findByProject(project1Id);
        Assert.assertEquals(1, resp.getBody().pictureList.size());
    }

    @Test
    public void t03_updatePersonsProjects() throws Exception {
        List<PersonFW> list = Arrays.asList(
                new PersonFW(danaId, null, null, null)
        );
        List<ProjectFW> pList = Arrays.asList(
                new ProjectFW(project1Id, null, null, null),
                new ProjectFW(project2Id, null, null, null)
        );
        ResponseEntity<PictureFullMsg> resp = controller.updatePersons(list, pictId);
        Assert.assertEquals(HttpStatus.OK, resp.getStatusCode());
        resp = controller.updateProjects(pList, pictId);
        Assert.assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    public void t03a_findPicts() {
        ResponseEntity<PictureListMsg> resp = controller.findByPerson(danaId);
        Assert.assertEquals(1, resp.getBody().pictureList.size());
        resp = controller.findByProject(project2Id);
        Assert.assertEquals(1, resp.getBody().pictureList.size());
        resp = controller.findByProject(project1Id);
        Assert.assertEquals(1, resp.getBody().pictureList.size());

    }

    @Test
    public void t98_deletePicture() throws Exception {
        ResponseEntity<PictureListMsg> resp = controller.delete(pictId);
        Assert.assertEquals(HttpStatus.OK, resp.getStatusCode());
        Assert.assertTrue(resp.getBody().pictureList.isEmpty());
    }

    @Test
    public void t99_clearData() throws Exception {
        TestData.clearData(personRepo, projectRepo);
    }


}
