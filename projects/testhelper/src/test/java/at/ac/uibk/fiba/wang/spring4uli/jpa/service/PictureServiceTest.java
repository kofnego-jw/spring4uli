package at.ac.uibk.fiba.wang.spring4uli.jpa.service;

import at.ac.uibk.fiba.wang.spring4uli.jpa.H2TestBase;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Picture;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;
import at.ac.uibk.fiba.wang.spring4uli.testhelper.TestData;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PictureServiceTest extends H2TestBase {

    private static final File SAMPLEIMAGES_DIR = new File("./src/test/resources/sampleimages");

    @Autowired
    private PictureService service;

    @Test
    public void t01_assumeNoData() {
        Assume.assumeTrue(service.listAll().isEmpty());
    }

    @Test
    public void t02_createData() throws Exception {
        TestData.writeData(personRepo, projectRepo);
    }

    @Test
    public void t03_createNewPictures() throws Exception {
        for (File now : SAMPLEIMAGES_DIR.listFiles()) {
            Set<Person> persons = new HashSet<>();
            Set<Project> projects = new HashSet<>();
            switch (now.getName().substring(now.getName().lastIndexOf(".") + 1)) {
                case "jpg":
                    persons.add(personRepo.findByName("Adam"));
                    projects.add(projectRepo.findByName("Project one"));
                case "png":
                    persons.add(personRepo.findByName("Berta"));
                    projects.add(projectRepo.findByName("Project two"));
                    break;
                case "tif":
                    persons.add(personRepo.findByName("Chris"));
            }
            service.save(now.getName(), now, persons, projects, true);
        }
    }

    @Test
    public void t04_readPictures() {
        List<String> paths = service.listAll();
        Assert.assertEquals(4, paths.size());
        Picture p = service.findOne("sample.jpg");
        Assert.assertNotNull(p);
        Assert.assertTrue(p.getPersons().size()==2);
        Assert.assertTrue(p.getProjects().size()==2);
        Assert.assertTrue(p.getPersons().contains(personRepo.findByName("Berta")));
    }

    @Test
    public void t05_listRelevantPictures() {
        List<String> picts = service.listAllContainingPerson(personRepo.findByName("Chris"));
        System.out.println(picts);
        Assert.assertTrue(picts.contains("sample.tif"));
        Assert.assertTrue(picts.size()==1);
        picts = service.listAllContainingProject(projectRepo.findByName("Project two"));
        Assert.assertEquals(2, picts.size());
    }

    @Test
    public void t06_deletePictures() {
        List<String> paths = service.listAll();
        paths.stream()
                .forEach(x -> service.delete(x));
    }

    @Test
    public void t07_assertNoMorePictures() {
        List<String> paths = service.listAll();
        Assert.assertTrue(paths.isEmpty());
    }

    @Test
    public void t99_clearData() throws Exception {
        TestData.clearData(personRepo, projectRepo);
    }


}
