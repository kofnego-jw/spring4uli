package at.ac.uibk.fiba.wang.spring4uli.rest.web;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Picture;
import at.ac.uibk.fiba.wang.spring4uli.jpa.service.PictureService;
import at.ac.uibk.fiba.wang.spring4uli.rest.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.PrintWriter;

@Controller
@RequestMapping("/images")
public class ImageServeController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private PictureService pictureService;


    @RequestMapping("/thumbs/{path:.+}")
    public void serveThumb(@PathVariable("path") String path, HttpServletResponse resp) {
        Picture p = pictureService.findOne(path);
        if (p==null) {
            sendNotFound(resp, "Cannot find thumb '" + path + "'");
            return;
        }
        byte[] thumb = imageService.createThumb(p);
        if (thumb.length==0) {
            sendError(resp, "Cannot create thumbnail.");
            return;
        }
        resp.setContentLength(thumb.length);
        resp.setContentType("image/jpeg");
        try (OutputStream os = resp.getOutputStream()) {
            os.write(thumb);
        } catch (Exception e) {
            sendError(resp, e.getMessage());
        }
    }

    @RequestMapping("/files/{path:.+}")
    public void serveFile(@PathVariable("path") String path, HttpServletResponse response) {
        Picture p = pictureService.findOne(path);
        if (p==null) {
            sendNotFound(response, "Cannot find file '" + path + "'.");
            return;
        }
        String contentType;
        String disposition;
        switch(p.getType()) {
            case JPEG: contentType = "image/jpeg"; disposition = "inline"; break;
            case TIFF: contentType = "image/tiff"; disposition = "attachment;filename=" + p.getPath(); break;
            case PNG:  contentType = "image/png"; disposition = "inline"; break;
            default: contentType = "application/octet-stream"; disposition = "attachment;filename=" + p.getPath();
        }
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", disposition);
        response.setContentLength(p.getContent().length);
        try (OutputStream os = response.getOutputStream()){
            os.write(p.getContent());
        } catch (Exception e) {
            sendError(response, e.getMessage());
        }
    }

    private static void sendError(HttpServletResponse resp, String msg) {
        send(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + msg);
    }

    private static void sendNotFound(HttpServletResponse resp, String msg) {
        send(resp, HttpServletResponse.SC_NOT_FOUND, msg);
    }

    private static void send(HttpServletResponse resp, int status, String msg) {
        resp.setStatus(status);
        try (PrintWriter writer = resp.getWriter()){
            writer.write(msg);
        } catch (Exception ignored) {

        }
    }

}
