package at.ac.uibk.fiba.wang.spring4uli.rest.web;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.*;
import at.ac.uibk.fiba.wang.spring4uli.jpa.service.PersonService;
import at.ac.uibk.fiba.wang.spring4uli.jpa.service.PictureService;
import at.ac.uibk.fiba.wang.spring4uli.jpa.service.ProjectService;
import at.ac.uibk.fiba.wang.spring4uli.rest.service.ImageService;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/picture")
public class PictureController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private PersonService personService;

    @Autowired
    private ProjectService projectService;

    @RequestMapping(value = "", method = {RequestMethod.GET})
    public ResponseEntity<PictureListMsg> listAll() {
        List<PictureProjection> paths = pictureService.listAll();
        return ResponseEntity.ok(new PictureListMsg(PictureFW.createPictureFWs(paths)));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.GET})
    public ResponseEntity<PictureFullMsg> findOne(@PathVariable("id") Long id) {
        Picture pict = pictureService.findOne(id);
        if (pict==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PictureFullMsg("Cannot find a picture with ID " + id + "."));
        byte[] thumb = imageService.createThumb(pict);
        return ResponseEntity.ok(new PictureFullMsg(PictureFull.createPictureFull(pict, thumb)));
    }

    @RequestMapping("/byPerson/{id}")
    public ResponseEntity<PictureListMsg> findByPerson(@PathVariable("{id}") Long personId) {
        Person p = personService.findOne(personId);
        if (p==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PictureListMsg("Cannot find person with ID " + personId + "."));
        }
        List<PictureProjection> picts = pictureService.listAllContainingPerson(p);
        return ResponseEntity.ok(new PictureListMsg(PictureFW.createPictureFWs(picts)));
    }

    @RequestMapping("/byProject/{id}")
    public ResponseEntity<PictureListMsg> findByProject(@PathVariable("{id}") Long projectId) {
        Project p = projectService.findOne(projectId);
        if (p==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PictureListMsg("Cannot find project with ID " + projectId + "."));
        }
        List<PictureProjection> picts = pictureService.listAllContainingProject(p);
        return ResponseEntity.ok(new PictureListMsg(PictureFW.createPictureFWs(picts)));
    }

    @RequestMapping(value = "", method = {RequestMethod.POST})
    public ResponseEntity<PictureFullMsg> create(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "persons", required = false, defaultValue = "") String persons,
            @RequestParam(value = "projects", required = false, defaultValue = "") String projects) {
        Picture pict;
        try {
            pict = createPicture(file);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PictureFullMsg("Cannot read uploaded file: " + e.getMessage()));
        }
        return createOrUpdate(pict, persons, projects);
    }

    @RequestMapping(value = "", method = {RequestMethod.PUT})
    public ResponseEntity<PictureFullMsg> create(@RequestBody PictureFull pict) {
        if (pict.id!=null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PictureFullMsg("Cannot create picture with id."));
        }
        if (pict.content==null || pict.content.length==0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PictureFullMsg("Cannot create picture without content."));
        }
        Picture picture = new Picture();
        picture.setPath(pict.path);
        picture.setType(stringToType(pict.type));
        picture.setPersons(toPersonSet(pict.persons));
        picture.setProjects(toProjectSet(pict.projects));
        picture.setContent(pict.content);
        try {
            picture = pictureService.saveOrUpdate(picture);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PictureFullMsg("Cannot create new picture: " + e.getMessage()));
        }
        byte[] thumb = imageService.createThumb(picture);
        return ResponseEntity.ok(new PictureFullMsg(PictureFull.createPictureFull(picture, thumb)));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PATCH})
    public ResponseEntity<PictureFullMsg> updateUsingJSON(@RequestBody PictureFull pict, @PathVariable("id") Long id) {
        if (!id.equals(pict.id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PictureFullMsg("Cannot update picture if ids are inconsistent."));
        }
        Picture picture = pictureService.findOne(id);
        if (picture==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PictureFullMsg("Cannot update picture, no picture with this id found."));
        }
        picture.setPath(pict.path);
        PictureType type = stringToType(pict.type);
        picture.setType(type);
        Set<Person> persons = toPersonSet(pict.persons);
        picture.setPersons(persons);
        Set<Project> projects = toProjectSet(pict.projects);
        picture.setProjects(projects);
        if (pict.content!=null) {
            picture.setContent(pict.content);
        }
        try {
            picture = pictureService.saveOrUpdate(picture);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PictureFullMsg("Cannot update picture: " + e.getMessage()));
        }
        byte[] thumb = imageService.createThumb(picture);
        return ResponseEntity.ok(new PictureFullMsg(PictureFull.createPictureFull(picture, thumb)));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<PictureFullMsg> update(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "persons", required = false, defaultValue = "") String persons,
            @RequestParam(value = "projects", required = false, defaultValue = "") String projects,
            @PathVariable("id") Long id) {
        Picture pict;
        try {
            pict = createPicture(file);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PictureFullMsg("Cannot read uploaded file: " + e.getMessage()));
        }
        pict.setId(id);
        return createOrUpdate(pict, persons, projects);
    }

    private static Picture createPicture(MultipartFile file) throws Exception {
        String path = file.getOriginalFilename();
        if (path==null) path = file.getName();
        byte[] content = file.getBytes();
        PictureType type = PictureType.guessType(path);
        return new Picture(path, type, content, null, null);
    }

    private ResponseEntity<PictureFullMsg> createOrUpdate(Picture p, String persons, String projects) {
        Set<Person> personSet = Stream.of(persons.split("\\D+"))
                .filter(x -> x != null && !x.isEmpty())
                .map(x -> Long.parseLong(x))
                .map(x -> personService.findOne(x))
                .filter(x -> x != null)
                .collect(Collectors.toSet());
        Set<Project> projectSet = Stream.of(projects.split("\\D+"))
                .filter(x -> x!=null && !x.isEmpty())
                .map(x -> Long.parseLong(x))
                .map(x -> projectService.findOne(x))
                .filter(x -> x!=null)
                .collect(Collectors.toSet());
        p.setPersons(personSet);
        p.setProjects(projectSet);
        try {
            p = pictureService.saveOrUpdate(p);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PictureFullMsg("Cannot save picture: " + e.getMessage()));
        }
        byte[] thumb = imageService.createThumb(p);
        return ResponseEntity.ok(new PictureFullMsg(PictureFull.createPictureFull(p, thumb)));
    }

    @RequestMapping(value = "/{id}/persons", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<PictureFullMsg> updatePersons(
            @RequestBody List<PersonFW> persons,
            @PathVariable("id") Long id) {
        Picture p = pictureService.findOne(id);
        if (p==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PictureFullMsg("Cannot find picture with ID " + id + "!"));
        }
        Set<Person> ps = persons.stream()
                .map(x -> x.toPerson())
                .collect(Collectors.toSet());
        try {
            p.setPersons(ps);
            p = pictureService.saveOrUpdate(p);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PictureFullMsg("Cannot update picture: " + e.getMessage()));
        }
        byte[] thumb = imageService.createThumb(p);
        return ResponseEntity.ok(new PictureFullMsg(PictureFull.createPictureFull(p, thumb)));
    }

    @RequestMapping(value = "/{id}/projects", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<PictureFullMsg> updateProjects(
            @RequestBody List<ProjectFW> projects,
            @PathVariable("id") Long id) {
        Picture p = pictureService.findOne(id);
        if (p==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PictureFullMsg("Cannot find picture with ID " + id + "!"));
        }
        Set<Project> ps = projects.stream()
                .map(x -> x.toProject())
                .collect(Collectors.toSet());
        try {
            p.setProjects(ps);
            p = pictureService.saveOrUpdate(p);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PictureFullMsg("Cannot update picture: " + e.getMessage()));
        }
        byte[] thumb = imageService.createThumb(p);
        return ResponseEntity.ok(new PictureFullMsg(PictureFull.createPictureFull(p, thumb)));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    public ResponseEntity<PictureListMsg> delete(@PathVariable("id") Long id) {
        try {
            pictureService.delete(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PictureListMsg("Cannot delete picture with ID " + id + ": " + e.getMessage()));
        }
        return listAll();
    }


    private static PictureType stringToType(String s) {
        if (s==null) return PictureType.UNKNOWN;
        try {
            return PictureType.valueOf(s.toUpperCase().trim());
        } catch (Exception e) {
            return PictureType.UNKNOWN;
        }
    }

    private static Set<Person> toPersonSet(List<PersonFW> persons) {
        return persons==null ? Collections.emptySet() :
                persons.stream()
                .map(p -> p.toPerson())
                .collect(Collectors.toSet());
    }

    private static Set<Project> toProjectSet(List<ProjectFW> projects) {
        return projects==null ? Collections.emptySet() :
                projects.stream()
                .map(x -> x.toProject())
                .collect(Collectors.toSet());
    }

}
