package at.ac.uibk.fiba.wang.spring4uli.rest.web.message;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;

public class ProjectFW {

    public final Long id;

    public final String name;

    public final String goal;

    public final PersonFW leader;

    protected ProjectFW(Long id, String name, String goal, PersonFW leader) {
        this.id = id;
        this.name = name;
        this.goal = goal;
        this.leader = leader;
    }

    public static ProjectFW createProjectFW(Project p) {
        if (p==null) return null;
        return new ProjectFW(p.getId(), p.getName(), p.getGoal(), PersonFW.createPersonFW(p.getLeader()));
    }
}
