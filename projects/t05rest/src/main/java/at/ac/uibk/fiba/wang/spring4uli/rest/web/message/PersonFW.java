package at.ac.uibk.fiba.wang.spring4uli.rest.web.message;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;

public class PersonFW {

    public final Long id;

    public final String name;

    public final String email;

    public final String info;

    protected PersonFW(Long id, String name, String email, String info) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.info = info;
    }

    public static PersonFW createPersonFW(Person p) {
        if (p==null) return null;
        return new PersonFW(p.getId(), p.getName(), p.getEmail(), p.getInfo());
    }
}
