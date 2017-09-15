package at.ac.uibk.fiba.wang.spring4uli.rest.web.message;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProjectFull extends ProjectFW {

    public final List<PersonFW> laborators;

    @JsonCreator
    public ProjectFull(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name,
            @JsonProperty("goal") String goal,
            @JsonProperty("leader") PersonFW leader,
            @JsonProperty("laborators") List<PersonFW> laborators) {
        super(id, name, goal, leader);
        this.laborators = laborators;
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

    public static ProjectFull createProjectFull(Project p) {
        if (p==null) return null;
        return new ProjectFull(p.getId(), p.getName(), p.getGoal(),
                PersonFW.createPersonFW(p.getLeader()), PersonFW.createPersonFWs(p.getLaborators()));
    }
}
