package at.ac.uibk.fiba.wang.spring4uli.jpa.service;

import at.ac.uibk.fiba.wang.spring4uli.jpa.MyJpaException;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Picture;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.PictureType;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;
import at.ac.uibk.fiba.wang.spring4uli.jpa.repository.PictureRepo;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class PictureService {

    private final PictureRepo pictureRepo;

    private final PersonService personService;

    private final ProjectService projectService;

    @Autowired
    public PictureService(PictureRepo pictureRepo, PersonService personService, ProjectService projectService) {
        this.pictureRepo = pictureRepo;
        this.personService = personService;
        this.projectService = projectService;
    }

    public Picture findOne(Long id) {
        return pictureRepo.findOne(id);
    }

    public Picture findOne(String path) {
        return pictureRepo.findByPath(path);
    }

    public List<String> listAll() {
        return pictureRepo.listAllPaths();
    }

    public List<String> listAllContainingPerson(Person p) {
        if (p==null) return Collections.emptyList();
        return pictureRepo.findAllPicturesContainingPerson(p);
    }

    public List<String> listAllContainingProject(Project project) {
        if (project==null) return Collections.emptyList();
        return pictureRepo.findAllPicturesContainingProject(project);
    }

    public Picture save(String path, File file, Set<Person> persons, Set<Project> projects, boolean overwrite)
            throws MyJpaException {
        if (!file.exists() || !file.isFile() || !file.canRead())
            throw new MyJpaException("Cannot read file, cannot save picture to database.");
        byte[] content;
        try {
            content = FileUtils.readFileToByteArray(file);
        } catch (Exception e) {
            throw new MyJpaException("Cannot read file.", e);
        }
        Picture inDb = findOne(path);
        if (inDb!=null) {
            if (overwrite) {
                pictureRepo.delete(inDb);
            } else {
                throw new MyJpaException("File already exists in the path.");
            }
        }
        Set<Project> inDbProjects = projectService.saveOrGet(projects);
        Set<Person> inDbPersons = personService.saveOrGet(persons);
        PictureType type = PictureType.guessType(file.getName());
        Picture p = new Picture(path, type, content, inDbPersons, inDbProjects);
        return pictureRepo.save(p);
    }

    public void delete(String path) {
        Picture p = findOne(path);
        if (p!=null) {
            pictureRepo.delete(p);
        }
    }


}
