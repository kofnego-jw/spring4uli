package at.ac.uibk.fiba.wang.spring4uli.rest;

import at.ac.uibk.fiba.wang.spring4uli.jpa.repository.PersonRepo;
import at.ac.uibk.fiba.wang.spring4uli.jpa.repository.ProjectRepo;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestSpringAppDefinition.class})
@PropertySource("/h2test.properties")
public class H2TestBase {

    @Autowired
    protected PersonRepo personRepo;

    @Autowired
    protected ProjectRepo projectRepo;



}
