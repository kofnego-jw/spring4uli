package at.ac.uibk.fiba.wang.spring4uli.rest.web.message;

public class ProjectFullMsg extends BaseMsg {

    public final ProjectFull project;

    public ProjectFullMsg(ProjectFull project) {
        this.project = project;
    }

    public ProjectFullMsg(String message) {
        super(message);
        this.project = null;
    }
}
