package at.ac.uibk.fiba.wang.spring4uli.rest.web;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;
import at.ac.uibk.fiba.wang.spring4uli.jpa.service.ProjectService;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.ProjectFW;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.ProjectFull;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.ProjectFullMsg;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.ProjectListMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping("")
    public ResponseEntity<ProjectListMsg> listAll() {
        List<Project> all = projectService.findAll();
        return ResponseEntity.ok(new ProjectListMsg(ProjectFW.createProjectFWList(all)));
    }

    @RequestMapping("/{id}")
    public ResponseEntity<ProjectFullMsg> findOne(@PathVariable("id") Long id) {
        Project p = projectService.findOne(id);
        if (p==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ProjectFullMsg("Cannot find a project with ID " + id + "."));
        return ResponseEntity.ok(new ProjectFullMsg(ProjectFull.createProjectFull(p)));
    }

    @RequestMapping(value = "", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<ProjectFullMsg> create(@RequestBody ProjectFull p) {
        if (p==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ProjectFullMsg("Cannot create project without information."));
        }
        return createOrUpdate(p.toProject());
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<ProjectFullMsg> update(@RequestBody ProjectFull project, @PathVariable("id") Long id) {
        if (project==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ProjectFullMsg("Cannot update project without information."));
        }
        if (!id.equals(project.id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ProjectFullMsg("Cannot update project with inconsistent id."));
        }
        return createOrUpdate(project.toProject());
    }

    private ResponseEntity<ProjectFullMsg> createOrUpdate(Project project) {
        try {
            Project saved = projectService.saveOrUpdate(project);
            return ResponseEntity.ok(new ProjectFullMsg(ProjectFull.createProjectFull(saved)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProjectFullMsg("Cannot save project: " + e.getMessage()));
        }
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    public ResponseEntity<ProjectListMsg> delete(@PathVariable("id") Long id) {
        try {
            projectService.delete(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProjectListMsg("Cannot delete project with ID " + id + ": " + e.getMessage()));
        }
        return listAll();
    }
}
