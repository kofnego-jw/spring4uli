package at.ac.uibk.fiba.wang.spring4uli.rest.web.message;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;
import at.ac.uibk.fiba.wang.spring4uli.jpa.service.ProjectFullInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProjectFull extends ProjectFW {

    public final List<PersonFW> laborators;

    public final List<PictureFW> inPictures;

    @JsonCreator
    public ProjectFull(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name,
            @JsonProperty("goal") String goal,
            @JsonProperty("leader") PersonFW leader,
            @JsonProperty("laborators") List<PersonFW> laborators,
            @JsonProperty("inPictures") List<PictureFW> inPictures) {
        super(id, name, goal, leader);
        this.laborators = laborators;
        this.inPictures = inPictures;
    }

    public Project toProject() {
        Project p = new Project(name, goal, null, null);
        p.setId(id);
        if (leader!=null) p.setLeader(leader.toPerson());
        if (laborators!=null) {
            Set<Person> labors = laborators
                    .stream()
                    .map(x -> x.toPerson())
                    .collect(Collectors.toSet());
            p.setLaborators(labors);
        }
        return p;
    }

    public static ProjectFull createProjectFull(ProjectFullInfo pFull) {
        if (pFull==null) return null;
        Project p = pFull.project;
        return new ProjectFull(p.getId(), p.getName(), p.getGoal(),
                PersonFW.createPersonFW(p.getLeader()), PersonFW.createPersonFWs(p.getLaborators()),
                PictureFW.createPictureFWs(pFull.inPictures));
    }
}
