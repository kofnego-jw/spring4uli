package at.ac.uibk.fiba.wang.spring4uli.jpa.service;

import at.ac.uibk.fiba.wang.spring4uli.jpa.H2TestBase;
import at.ac.uibk.fiba.wang.spring4uli.jpa.MyJpaException;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
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
    public void t01_noData() {
        List<Person> list = service.findAll();
        Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void t02_createPerson() throws Exception {
        Person p = new Person("Name", "email@example.com", "info info");
        p = service.saveOrUpdate(p);
        Assert.assertEquals(1L, p.getId().longValue());
    }

    @Test(expected = MyJpaException.class)
    public void t03_createPersonWithSameName() throws Exception {
        Person p = new Person("Name", "nomail@nowhere.com", "info again");
        p = service.saveOrUpdate(p);
    }


}
