package at.ac.uibk.fiba.wang.spring4uli.jpa.ontology;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "PROJECTS")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME", unique = true)
    private String name;

    @Column(name = "GOAL", length = 2000)
    private String goal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LEADER_ID")
    private Person leader;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "PROJECTS_LABORATORS", joinColumns = {
            @JoinColumn(name = "PROJECT_ID")
    }, inverseJoinColumns = {
            @JoinColumn(name = "LABORATOR_ID")
    })
    private Set<Person> laborators;

    public Project() {
    }

    public Project(String name, String goal, Person leader, Set<Person> laborators) {
        this.name = name;
        this.goal = goal;
        this.leader = leader;
        this.laborators = laborators;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public Person getLeader() {
        return leader;
    }

    public void setLeader(Person leader) {
        this.leader = leader;
    }

    public Set<Person> getLaborators() {
        return laborators;
    }

    public void setLaborators(Set<Person> laborators) {
        this.laborators = laborators;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (id != null ? !id.equals(project.id) : project.id != null) return false;
        return name != null ? name.equals(project.name) : project.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", goal='" + goal + '\'' +
                ", leader=" + leader +
                ", laborators=" + laborators +
                '}';
    }
}
