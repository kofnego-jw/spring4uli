package at.ac.uibk.fiba.wang.spring4uli.rest.web.message;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Picture;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.PictureType;

import java.util.List;

public class PictureFull extends PictureFW {

    public final String type;

    public final List<PersonFW> persons;

    public final List<ProjectFW> projects;

    public final byte[] content;

    public final byte[] thumb;

    public PictureFull(Long id, String path, String type, List<PersonFW> persons, List<ProjectFW> projects,
                       byte[] content, byte[] thumb) {
        super(id, path);
        this.type = type;
        this.persons = persons;
        this.projects = projects;
        this.content = content;
        this.thumb = thumb;
    }

    public static PictureFull createPictureFull(Picture p, byte[] thumb) {
        String type = p.getType()==null ? PictureType.UNKNOWN.name() : p.getType().name();
        List<PersonFW> persons = PersonFW.createPersonFWs(p.getPersons());
        List<ProjectFW> projects = ProjectFW.createProjectFWList(p.getProjects());
        return new PictureFull(p.getId(), p.getPath(), type, persons, projects, p.getContent(), thumb);
    }
}
