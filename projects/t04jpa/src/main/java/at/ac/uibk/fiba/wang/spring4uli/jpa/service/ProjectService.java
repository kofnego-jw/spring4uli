package at.ac.uibk.fiba.wang.spring4uli.jpa.service;

import at.ac.uibk.fiba.wang.spring4uli.jpa.MyJpaException;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;
import at.ac.uibk.fiba.wang.spring4uli.jpa.repository.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepo projectRepo;

    @Autowired
    public ProjectService(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
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

    public Project saveOrUpdate(Project p) throws MyJpaException {
        if (p==null) return null;
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

}
