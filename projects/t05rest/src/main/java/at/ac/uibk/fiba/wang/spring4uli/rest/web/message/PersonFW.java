package at.ac.uibk.fiba.wang.spring4uli.rest.web.message;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PersonFW {

    public final Long id;

    public final String name;

    public final String email;

    public final String info;

    @JsonCreator
    public PersonFW(@JsonProperty("id") Long id,
                    @JsonProperty("name") String name,
                    @JsonProperty("email") String email,
                    @JsonProperty("info") String info) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.info = info;
    }

    public Person toPerson() {
        Person person = new Person(name, email, info);
        person.setId(id);
        return person;
    }

    public static PersonFW createPersonFW(Person p) {
        if (p==null) return null;
        return new PersonFW(p.getId(), p.getName(), p.getEmail(), p.getInfo());
    }

    public static List<PersonFW> createPersonFWs(Collection<Person> ps) {
        if (ps==null) return Collections.emptyList();
        return ps.stream()
                .map(x -> createPersonFW(x))
                .filter(x -> x!=null)
                .collect(Collectors.toList());
    }
}
