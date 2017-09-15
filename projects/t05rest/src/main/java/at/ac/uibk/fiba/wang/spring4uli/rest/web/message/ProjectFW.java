package at.ac.uibk.fiba.wang.spring4uli.rest.web.message;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectFW {

    public final Long id;

    public final String name;

    public final String goal;

    public final PersonFW leader;

    @JsonCreator
    public ProjectFW(@JsonProperty("id") Long id,
                     @JsonProperty("name") String name,
                     @JsonProperty("goal") String goal,
                     @JsonProperty("leader") PersonFW leader) {
        this.id = id;
        this.name = name;
        this.goal = goal;
        this.leader = leader;
    }

    public Project toProject() {
        Project p = new Project(name, goal, leader==null ? null : leader.toPerson(), Collections.emptySet());
        p.setId(id);
        return p;
    }

    public static ProjectFW createProjectFW(Project p) {
        if (p==null) return null;
        return new ProjectFW(p.getId(), p.getName(), p.getGoal(), PersonFW.createPersonFW(p.getLeader()));
    }

    public static List<ProjectFW> createProjectFWList(Collection<Project> list) {
        if (list==null) return Collections.emptyList();
        return list
                .stream()
                .map(x -> createProjectFW(x))
                .filter(x -> x!=null)
                .collect(Collectors.toList());
    }
}
