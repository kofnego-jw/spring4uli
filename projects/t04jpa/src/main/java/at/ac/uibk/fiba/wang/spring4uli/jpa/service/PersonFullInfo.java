package at.ac.uibk.fiba.wang.spring4uli.jpa.service;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.PictureProjection;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;

import java.util.List;

public class PersonFullInfo {

    public final Person person;

    public final List<Project> asLeader;

    public final List<Project> asLaborator;

    public final List<PictureProjection> inPictures;

    public PersonFullInfo(Person person, List<Project> asLeader, List<Project> asLaborator, List<PictureProjection> inPictures) {
        this.person = person;
        this.asLeader = asLeader;
        this.asLaborator = asLaborator;
        this.inPictures = inPictures;
    }
}
