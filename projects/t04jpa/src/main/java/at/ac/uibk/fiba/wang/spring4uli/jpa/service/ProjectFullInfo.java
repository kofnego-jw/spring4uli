package at.ac.uibk.fiba.wang.spring4uli.jpa.service;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.PictureProjection;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;

import java.util.List;

public class ProjectFullInfo {

    public final Project project;

    public final List<PictureProjection> inPictures;

    public ProjectFullInfo(Project project, List<PictureProjection> inPictures) {
        this.project = project;
        this.inPictures = inPictures;
    }
}
