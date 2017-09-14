package at.ac.uibk.fiba.wang.spring4uli.jpa.service;

import at.ac.uibk.fiba.wang.spring4uli.jpa.MyJpaException;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;
import at.ac.uibk.fiba.wang.spring4uli.jpa.repository.ProjectRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepo projectRepo;

    private final PersonService personService;

    @Autowired
    public ProjectService(ProjectRepo projectRepo, PersonService service) {
        this.projectRepo = projectRepo;
        this.personService = service;
    }

    public List<Project> findAll() {
        return projectRepo.findAll();
    }

    public Project findOne(Long id) {
        return projectRepo.findOne(id);
    }

    public Project findOne(String name) {
        return projectRepo.findByName(name);
    }

    private Project mergeToDb(Project p) {
        Person leader = personService.saveOrGet(p.getLeader());
        Set<Person> labors = personService.saveOrGet(p.getLaborators());
        p.setLeader(leader);
        p.setLaborators(labors);
        return p;
    }

    public Project saveOrUpdate(Project p) throws MyJpaException {
        if (p==null) return null;
        mergeToDb(p);
        if (p.getId()!=null) {
            return projectRepo.save(p);
        }
        Project inDb = projectRepo.findByName(p.getName());
        if (inDb!=null)
            throw new MyJpaException("There is already a project with this name.");
        return projectRepo.save(p);
    }

    public void delete(Long id) {
        projectRepo.delete(id);
    }


    public Project saveOrGet(Project p) {
        if (p==null) return null;
        if (p.getId()!=null) return findOne(p.getId());
        try {
            return saveOrUpdate(p);
        } catch (Exception e) {
            LOGGER.error("Cannot saveOrGet a project.", e);
            return null;
        }
    }

    public Set<Project> saveOrGet(Set<Project> ps) {
        if (ps==null) return Collections.emptySet();
        return ps.stream()
                .map(x -> saveOrGet(x))
                .filter(x -> x!=null)
                .collect(Collectors.toSet());
    }

}
