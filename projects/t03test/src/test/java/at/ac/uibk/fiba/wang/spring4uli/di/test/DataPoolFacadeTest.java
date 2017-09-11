package at.ac.uibk.fiba.wang.spring4uli.di.test;

import at.ac.uibk.fiba.wang.spring4uli.di.service.DataPool;
import at.ac.uibk.fiba.wang.spring4uli.test.TestSpringAppDefinition;
import at.ac.uibk.fiba.wang.spring4uli.test.service.DataPoolFacade;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestSpringAppDefinition.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataPoolFacadeTest {

    private static final String USERNAME = "Test";
    private static final String USERNAME2 = "TEST";

    @Autowired
    DataPool dataPool;

    @Autowired
    DataPoolFacade facade;

    @Test
    public void t01_createUser() throws Exception {
        dataPool.putUsername(USERNAME);
    }

    @Test
    public void t02_facadeSayUsernameExists() {
        Assert.assertTrue(facade.isRegistered(USERNAME));
    }

    @Test
    public void t03_facadeSayUsernameNotExists() {
        Assert.assertFalse(facade.isRegistered(USERNAME2));
    }

}
