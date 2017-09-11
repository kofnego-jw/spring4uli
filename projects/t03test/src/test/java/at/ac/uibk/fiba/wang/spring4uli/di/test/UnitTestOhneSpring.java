package at.ac.uibk.fiba.wang.spring4uli.di.test;

import at.ac.uibk.fiba.wang.spring4uli.di.service.DataPool;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UnitTestOhneSpring {

    private static final String USERNAME = "Test";

    private static DataPool dataPool;

    @BeforeClass
    public static void setUpDataPool() throws Exception {
        dataPool = new DataPool();
    }

    private static Long testUserId;

    @Test
    public void t01_addUser() throws Exception {
        testUserId = dataPool.putUsername(USERNAME);
        Assert.assertTrue(testUserId == 1L);
    }

    @Test
    public void t02_readUserById() throws Exception {
        Assert.assertEquals(testUserId, dataPool.getId(USERNAME));
    }

    @Test
    public void t03_readUserByName() throws Exception {
        Assert.assertEquals(USERNAME, dataPool.getUsername(testUserId));
    }

    @Test(expected = Exception.class)
    public void t04_addUsernameAgainShouldThrowException() throws Exception {
        dataPool.putUsername(USERNAME);
    }

}
