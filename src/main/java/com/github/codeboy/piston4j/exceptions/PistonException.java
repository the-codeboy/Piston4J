package com.github.codeboy.piston4j.exceptions;

public class PistonException extends RuntimeException {

    public PistonException(String message) {
        super(message);
    }

    public PistonException() {
        super();
    }

    public PistonException(Throwable cause) {
        super(cause);
    }
}
