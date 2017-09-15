package at.ac.uibk.fiba.wang.spring4uli.rest.service;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Picture;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.PictureType;
import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ImageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


@Component
public class ImageService {

    private static final int DEFAULT_WIDTH = 200;

    private static final int DEFAULT_HEIGHT = 200;

    private static final String DEFAULT_FORMAT = "JPEG";

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);

    private final Opener opener;

    public ImageService() {
        opener = new Opener();
    }

    public byte[] createThumb(Picture picture) {
        if (picture == null) return new byte[0];
        try {
            return createThumb(picture.getType(), picture.getPath(), picture.getContent(), DEFAULT_WIDTH, DEFAULT_HEIGHT);
        } catch (Exception e) {
            LOGGER.error("Cannot create thumbnail.", e);
            return new byte[0];
        }
    }

    public byte[] createThumb(PictureType type, String name, byte[] content, int width, int height) throws Exception {
        if (type == PictureType.UNKNOWN) return new byte[0];
        ImagePlus ip;
        ByteArrayInputStream in = new ByteArrayInputStream(content);
        switch (type) {
            case TIFF:
                ip = opener.openTiff(in, name);
                break;
            case JPEG:
            case PNG:
                BufferedImage bi = ImageIO.read(in);
                ip = new ImagePlus(name, bi);
                break;
            default:
                ip = null;
        }
        return resize(ip, width, height);
    }

    public byte[] resize(ImagePlus ip, int width, int height) throws Exception {
        if (ip==null) return new byte[0];
        float rfX, rfY;
        rfX = (float) width / (float) ip.getWidth();
        rfY = (float) height / (float) ip.getHeight();
        float rf = (rfX < rfY) ? rfX : rfY;
        int w = (int) (ip.getWidth() * rf);
        int h = (int) (ip.getHeight() * rf);
        ImageProcessor resized = ip.getProcessor().convertToRGB().resize(w, h, true);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(resized.getBufferedImage(), DEFAULT_FORMAT, os);
        return os.toByteArray();
    }


}
