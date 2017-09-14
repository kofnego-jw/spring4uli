package at.ac.uibk.fiba.wang.spring4uli.jpa.ontology;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PICTURES")
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "PATH", unique = true)
    private String path;

    @Enumerated(EnumType.STRING)
    private PictureType type;

    @Lob
    @Column(name = "CONTENT")
    private byte[] content;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "PICTURE_PERSON", joinColumns = {
            @JoinColumn(name = "PICTURE_ID")
    }, inverseJoinColumns = {
            @JoinColumn(name = "PERSON_ID")
    })
    private Set<Person> persons = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "PICTURE_PROJECT", joinColumns = {
            @JoinColumn(name = "PICTURE_ID")
    }, inverseJoinColumns = {
            @JoinColumn(name = "PROJECT_ID")
    })
    private Set<Project> projects = new HashSet<>();

    public Picture() {
    }

    public Picture(String path, PictureType type, byte[] content, Set<Person> persons, Set<Project> projects) {
        this.path = path;
        this.type = type;
        this.content = content;
        this.persons = persons;
        this.projects = projects;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public PictureType getType() {
        return type;
    }

    public void setType(PictureType type) {
        this.type = type;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", type=" + type +
                ", content=" + Arrays.toString(content) +
                ", persons=" + persons +
                ", projects=" + projects +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Picture picture = (Picture) o;

        if (id != null ? !id.equals(picture.id) : picture.id != null) return false;
        return path != null ? path.equals(picture.path) : picture.path == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }
}
