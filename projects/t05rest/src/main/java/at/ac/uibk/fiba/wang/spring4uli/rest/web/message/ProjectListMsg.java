package at.ac.uibk.fiba.wang.spring4uli.rest.web.message;

import java.util.Collections;
import java.util.List;

public class ProjectListMsg extends BaseMsg {

    public final List<ProjectFW> projectList;

    public ProjectListMsg(List<ProjectFW> list) {
        this.projectList = list;
    }

    public ProjectListMsg(String message) {
        super(message);
        this.projectList = Collections.emptyList();
    }
}
