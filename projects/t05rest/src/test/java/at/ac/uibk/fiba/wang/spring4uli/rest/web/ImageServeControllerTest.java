package at.ac.uibk.fiba.wang.spring4uli.rest.web;

import at.ac.uibk.fiba.wang.spring4uli.rest.RestSpringAppDefinition;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.PictureFW;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.PictureFullMsg;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.PictureListMsg;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(classes = {RestSpringAppDefinition.class})
@PropertySource("classpath:/h2test.properties")
@RunWith(SpringJUnit4ClassRunner.class)
public class ImageServeControllerTest  {

    private static final File SAMPLE_DIR = new File("../testhelper/src/test/resources/sampleimages");

    @Autowired
    private PictureController pictureController;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ImageServeController imageServeController;

    private static Long pId[] = new Long[4];

    @BeforeClass
    public static void assumeData() {
        File[] files = SAMPLE_DIR.listFiles();
        Assume.assumeTrue(files.length==4);
    }

    @Test
    public void t01_createData() throws Exception {
        File[] files = SAMPLE_DIR.listFiles();
        for (int i=0; i< files.length; i++) {
            File now = files[i];
            MultipartFile m = new MockMultipartFile(now.getName(), now.getName(), "", FileUtils.readFileToByteArray(now));
            ResponseEntity<PictureFullMsg> resp = pictureController.create(m, "", "");
            Assert.assertEquals(HttpStatus.OK, resp.getStatusCode());
            pId[i] = resp.getBody().picture.id;
        }
    }

    @Test
    public void t02_readData() throws Exception {
        ResponseEntity<PictureListMsg> resp = pictureController.listAll();
        Assert.assertEquals(4, resp.getBody().pictureList.size());
        for (PictureFW fw: resp.getBody().pictureList) {
            mockMvc.perform(MockMvcRequestBuilders.get("/images/files/" + fw.path))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

            ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get("/images/thumbs/" + fw.path));
            if (!fw.path.startsWith("sample")) {
                action.andExpect(MockMvcResultMatchers.status().is5xxServerError());
            } else {
                action.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
            }
        }
    }

    @Test
    public void t99_deleteData() {
        for (PictureFW fw: pictureController.listAll().getBody().pictureList) {
            ResponseEntity<PictureListMsg> resp = pictureController.delete(fw.id);
            Assert.assertEquals(HttpStatus.OK, resp.getStatusCode());
        }

        ResponseEntity<PictureListMsg> resp = pictureController.listAll();
        Assert.assertEquals(0, resp.getBody().pictureList.size());
    }
}
