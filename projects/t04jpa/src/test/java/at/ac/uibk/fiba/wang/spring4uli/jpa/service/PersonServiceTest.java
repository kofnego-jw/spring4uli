package at.ac.uibk.fiba.wang.spring4uli.jpa.service;

import at.ac.uibk.fiba.wang.spring4uli.jpa.H2TestBase;
import at.ac.uibk.fiba.wang.spring4uli.jpa.MyJpaException;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import at.ac.uibk.fiba.wang.spring4uli.jpa.repository.PersonRepo;
import at.ac.uibk.fiba.wang.spring4uli.jpa.repository.PictureRepo;
import at.ac.uibk.fiba.wang.spring4uli.jpa.repository.ProjectRepo;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersonServiceTest extends H2TestBase {

    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private PictureRepo pictureRepo;

    @Autowired
    private PersonService service;

    private static Long personId;

    @Test
    public void t02_createPerson() throws Exception {
        Person p = new Person("Name2", "email2@example.com", "info info");
        p = service.saveOrUpdate(p);
        personId = p.getId();
    }

    @Test(expected = MyJpaException.class)
    public void t03_createPersonWithSameName() throws Exception {
        Person p = new Person("Name2", "nomail@nowhere.com", "info again");
        service.saveOrUpdate(p);
    }

    @Test
    public void t04_deletePerson() throws Exception {
        service.delete(personId);
        List<Person> all = service.findAll();
        Assert.assertTrue(all.isEmpty());
    }

}
