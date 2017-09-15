package at.ac.uibk.fiba.wang.spring4uli.rest.web.message;

import java.util.Collections;
import java.util.List;

public class PictureListMsg extends BaseMsg {

    public final List<PictureFW> pictureList;

    public PictureListMsg(List<PictureFW> pictureList) {
        this.pictureList = pictureList;
    }

    public PictureListMsg(String message) {
        super(message);
        this.pictureList = Collections.emptyList();
    }
}
