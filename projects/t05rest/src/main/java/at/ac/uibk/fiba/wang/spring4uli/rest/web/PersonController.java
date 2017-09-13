package at.ac.uibk.fiba.wang.spring4uli.rest.web;

import at.ac.uibk.fiba.wang.spring4uli.jpa.service.PersonFullInfo;
import at.ac.uibk.fiba.wang.spring4uli.jpa.service.PersonService;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.PersonFW;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.PersonFull;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.PersonFullMsg;
import at.ac.uibk.fiba.wang.spring4uli.rest.web.message.PersonListMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/{id}")
    public ResponseEntity<PersonFullMsg> findOne(@PathVariable("id") Long id) {
        PersonFullInfo info = personService.getFullInfo(id);
        if (info==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PersonFullMsg("Cannot find Person with id " + id + "."));
        }
        return ResponseEntity.ok(
                new PersonFullMsg(PersonFull.createPersonFull(info.person, info.asLeader, info.asLaborator))
        );
    }

}
