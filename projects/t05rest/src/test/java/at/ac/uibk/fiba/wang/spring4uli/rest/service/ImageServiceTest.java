package at.ac.uibk.fiba.wang.spring4uli.rest.service;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Picture;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.PictureType;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Collections;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ImageServiceTest {

    private static final ImageService service = new ImageService();

    private static final File IMAGES_DIR = new File("../testhelper/src/test/resources/sampleimages");

    @Test
    public void t01_createThumbs() throws Exception {
        for (File now: IMAGES_DIR.listFiles()) {
            Picture stub = createStub(now);
            byte[] result = service.createThumb(stub);
            switch(stub.getType()) {
                case UNKNOWN:
                    Assert.assertTrue(result.length == 0);
                    break;
                default:
                    Assert.assertTrue(result.length > 0);
                    BufferedImage bi = ImageIO.read(new ByteArrayInputStream(result));
                    Assert.assertTrue(bi.getWidth()<=200 && bi.getHeight() <= 200 &&
                            (bi.getWidth()==200 || bi.getHeight()==200));
            }
        }
    }

    private static Picture createStub(File f) throws Exception {
        byte[] content = FileUtils.readFileToByteArray(f);
        Picture p = new Picture(f.getName(), PictureType.guessType(f.getName()),
                content, Collections.emptySet(), Collections.emptySet());
        return p;
    }
}
