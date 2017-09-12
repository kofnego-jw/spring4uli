package at.ac.uibk.fiba.wang.spring4uli.jpa;

import org.junit.runner.RunWith;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaSpringAppDefinition.class})
@PropertySource("/h2test.properties")
public class H2TestBase {

}
