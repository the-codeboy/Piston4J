package com.github.codeboy.piston4j.exceptions;

import java.io.IOException;

public class PistonException extends RuntimeException {

    public PistonException(String message) {
        super(message);
    }

    public PistonException() {
        super();
    }
}
