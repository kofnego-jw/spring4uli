package at.ac.uibk.fiba.wang.spring4uli.jpa.service;

import at.ac.uibk.fiba.wang.spring4uli.jpa.MyJpaException;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;
import at.ac.uibk.fiba.wang.spring4uli.jpa.repository.PersonRepo;
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
public class PersonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepo repo;

    private final ProjectRepo projectRepo;

    @Autowired
    public PersonService(PersonRepo repo, ProjectRepo projectRepo) {
        this.repo = repo;
        this.projectRepo = projectRepo;
    }

    public List<Person> findAll() {
        return repo.findAll();
    }

    public Person findOne(Long id) {
        return repo.findOne(id);
    }

    public Person findOne(String name) {
        return repo.findByName(name);
    }

    public Person saveOrUpdate(Person p) throws MyJpaException {
        if (p==null) return null;
        if (p.getId()!=null) return repo.save(p);
        Person inDb = repo.findByName(p.getName());
        if (inDb!=null)
            throw new MyJpaException("Name already in database.");
        return repo.save(p);
    }

    public PersonFullInfo getFullInfo(Long id) {
        Person p = findOne(id);
        if (p==null) return null;
        List<Project> asLeader = projectRepo.findAllByLeader(p);
        List<Project> asLaborator = projectRepo.findAllByLaboratorsHavingPerson(p);
        return new PersonFullInfo(p, asLeader, asLaborator);
    }

    public void delete(Long id) {
        repo.delete(id);
    }

    public Person saveOrGet(Person p) {
        if (p==null) return null;
        if (p.getId()!=null) return findOne(p.getId());
        try {
            return saveOrUpdate(p);
        } catch (Exception e) {
            LOGGER.error("Cannot save a person.", e);
            return null;
        }
    }

    public Set<Person> saveOrGet(Set<Person> persons) {
        if (persons==null) return Collections.emptySet();
        return persons.stream()
                .map(x -> saveOrGet(x))
                .filter(x -> x!=null)
                .collect(Collectors.toSet());
    }

}
