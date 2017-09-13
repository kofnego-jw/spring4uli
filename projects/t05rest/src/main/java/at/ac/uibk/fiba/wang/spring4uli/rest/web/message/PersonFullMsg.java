package at.ac.uibk.fiba.wang.spring4uli.rest.web.message;

public class PersonFullMsg extends BaseMsg {

    public final PersonFull person;

    public PersonFullMsg(String message) {
        super(message);
        this.person = null;
    }

    public PersonFullMsg(PersonFull person) {
        this.person = person;
    }
}
