package at.ac.uibk.fiba.wang.spring4uli.jpa;

public class MyJpaException extends Exception {

    public MyJpaException() {
    }

    public MyJpaException(String message) {
        super(message);
    }

    public MyJpaException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyJpaException(Throwable cause) {
        super(cause);
    }
}
