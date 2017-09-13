package at.ac.uibk.fiba.wang.spring4uli.rest.web.message;

public abstract class BaseMsg {

    public final String message;

    public final boolean hasError;

    public BaseMsg(String message) {
        this.message = message;
        this.hasError = true;
    }

    public BaseMsg(String message, boolean hasError) {
        this.message = message;
        this.hasError = hasError;
    }

    public BaseMsg() {
        this.message = "ok";
        this.hasError = false;
    }
}
