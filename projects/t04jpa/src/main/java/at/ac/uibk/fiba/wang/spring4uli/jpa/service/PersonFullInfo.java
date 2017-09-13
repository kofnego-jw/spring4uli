package at.ac.uibk.fiba.wang.spring4uli.jpa.service;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;

import java.util.List;

public class PersonFullInfo {

    public final Person person;

    public final List<Project> asLeader;

    public final List<Project> asLaborator;

    public PersonFullInfo(Person person, List<Project> asLeader, List<Project> asLaborator) {
        this.person = person;
        this.asLeader = asLeader;
        this.asLaborator = asLaborator;
    }
}
