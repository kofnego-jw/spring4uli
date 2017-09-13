package at.ac.uibk.fiba.wang.spring4uli.rest.web.message;

import java.util.Collections;
import java.util.List;

public class PersonListMsg extends BaseMsg {

    public final List<PersonFW> personList;

    public PersonListMsg(List<PersonFW> personList) {
        this.personList = personList;
    }

    public PersonListMsg(String message) {
        super(message);
        this.personList = Collections.emptyList();
    }
}
