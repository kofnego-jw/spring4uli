package at.ac.uibk.fiba.wang.spring4uli.rest.web.message;

public class PictureFullMsg extends BaseMsg {

    public final PictureFull picture;

    public PictureFullMsg(PictureFull picture) {
        this.picture = picture;
    }

    public PictureFullMsg(String message) {
        super(message);
        this.picture = null;
    }
}
