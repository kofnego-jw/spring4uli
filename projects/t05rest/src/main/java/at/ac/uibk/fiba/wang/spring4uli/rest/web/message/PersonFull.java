package at.ac.uibk.fiba.wang.spring4uli.rest.web.message;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.PictureProjection;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;
import at.ac.uibk.fiba.wang.spring4uli.jpa.service.PersonFullInfo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PersonFull extends PersonFW {

    public final List<ProjectFW> asLeader;

    public final List<ProjectFW> asLaborator;

    public final List<PictureFW> inPictures;

    public PersonFull(PersonFW fw, List<ProjectFW> asLeader,
                      List<ProjectFW> asLaborator, List<PictureFW> inPictures) {
        super(fw.id, fw.name, fw.email, fw.info);
        this.asLeader = asLeader;
        this.asLaborator = asLaborator;
        this.inPictures = inPictures;
    }

    public static PersonFull createPersonFull(PersonFullInfo info) {
        if (info==null) return null;
        return createPersonFull(info.person, info.asLeader, info.asLaborator, info.inPictures);
    }

    public static PersonFull createPersonFull(Person p, List<Project> asLeader, List<Project> asLaborator,
                                              List<PictureProjection> inPictures) {
        if (p==null) return null;
        PersonFW fw = PersonFW.createPersonFW(p);
        List<ProjectFW> asLeaderFWs = asLeader==null ? Collections.emptyList() :
                asLeader.stream().map(x -> ProjectFW.createProjectFW(x))
                .filter(x -> x!=null).collect(Collectors.toList());
        List<ProjectFW> asLaboratorFWs = asLaborator==null ? Collections.emptyList() :
                asLaborator.stream().map(x -> ProjectFW.createProjectFW(x))
                .filter(x -> x!=null).collect(Collectors.toList());
        List<PictureFW> inPicts = inPictures ==null ? Collections.emptyList() :
                inPictures.stream().map(x -> PictureFW.createPictureFW(x))
                .filter(x -> x!=null).collect(Collectors.toList());
        return new PersonFull(fw, asLeaderFWs, asLaboratorFWs, inPicts);
    }
}
