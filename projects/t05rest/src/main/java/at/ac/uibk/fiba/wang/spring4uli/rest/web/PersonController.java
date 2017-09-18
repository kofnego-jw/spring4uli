package at.ac.uibk.fiba.wang.spring4uli.rest.web;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import at.ac.uibk.fiba.wang.spring4uli.jpa.service.PersonFullInfo;
import at.ac.uibk.fiba.wang.spring4uli.jpa.service.PersonService;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.PersonFW;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.PersonFull;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.PersonFullMsg;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.PersonListMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    @Autowired
    PersonService personService;

    @RequestMapping("")
    public ResponseEntity<PersonListMsg> findAll() {
        List<PersonFW> list = personService.findAll()
                .stream()
                .map(p -> PersonFW.createPersonFW(p))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new PersonListMsg(list));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.GET})
    public ResponseEntity<PersonFullMsg> findOne(@PathVariable("id") Long id) {
        PersonFullInfo info = personService.getFullInfo(id);
        if (info==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PersonFullMsg("Cannot find Person with id " + id + "."));
        }
        return ResponseEntity.ok(
                new PersonFullMsg(PersonFull.createPersonFull(info))
        );
    }

    @RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<PersonFullMsg> create(@RequestBody PersonFW personFW) {
        return createOrUpdate(personFW, null);
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<PersonFullMsg> update(@RequestBody PersonFW personFW, @PathVariable("id") Long id) {
        if (personFW.id == null || !personFW.id.equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PersonFullMsg("Cannot update person information if ID is not consistent."));
        }
        return createOrUpdate(personFW, id);
    }


    public ResponseEntity<PersonFullMsg> createOrUpdate(PersonFW personFW, Long id) {
        if (personFW==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PersonFullMsg("Cannot update person with null person information."));
        }
        Person p = personFW.toPerson();
        if (id!=null) p.setId(id);
        if (p.getName()==null || p.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PersonFullMsg("Cannot update person with no name."));

        }
        try {
            p = personService.saveOrUpdate(p);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PersonFullMsg("Cannot save or update person: " + e.getMessage()));
        }
        PersonFullInfo fullInfo = personService.getFullInfo(p.getId());
        return ResponseEntity.ok(new PersonFullMsg(PersonFull.createPersonFull(fullInfo)));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    public ResponseEntity<PersonListMsg> delete(@PathVariable("id") Long id) {
        try {
            personService.delete(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PersonListMsg("Cannot delete Person with ID " + id + ": " + e.getMessage()));
        }
        return findAll();
    }

}
