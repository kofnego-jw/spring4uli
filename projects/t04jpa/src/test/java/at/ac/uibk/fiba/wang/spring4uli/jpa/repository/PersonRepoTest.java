package at.ac.uibk.fiba.wang.spring4uli.jpa.repository;

import at.ac.uibk.fiba.wang.spring4uli.jpa.H2TestBase;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersonRepoTest extends H2TestBase {

    @Autowired
    protected PersonRepo personRepo;

    @Autowired
    protected ProjectRepo projectRepo;

    @Autowired
    protected PictureRepo pictureRepo;


//    @Test
//    public void t01_assertDatabaseIsEmpty() throws Exception {
//        List<Person> list = personRepo.findAll();
//        Assert.assertTrue(list.isEmpty());
//    }

    private static Long personId;

    @Test
    public void t02_createNewPerson() throws Exception {
        Person p = new Person("Name", "email@example.com", "info info info.");
        Person saved = personRepo.save(p);
        Assert.assertNotNull(saved);
        personId = saved.getId();
    }

    @Test
    public void t03_readOne() {
        Person one = personRepo.findOne(personId);
        Assert.assertEquals("Name", one.getName());
    }

    @Test
    public void t04_likeQuery() {
        List<Person> list = personRepo.findByNameContaining("Nam");
        Assert.assertEquals(1, list.size());
        Person p = personRepo.findByName("Name");
        Assert.assertNotNull(p);
    }

    @Test
    public void t05_delete() {
        personRepo.delete(personId);
    }
}
