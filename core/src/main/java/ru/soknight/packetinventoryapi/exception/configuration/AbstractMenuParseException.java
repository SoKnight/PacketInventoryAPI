package ru.soknight.packetinventoryapi.exception.configuration;

import lombok.Getter;

@Getter
public abstract class AbstractMenuParseException extends Exception {

    private final String fileName;

    public AbstractMenuParseException(String fileName, String message) {
        this(fileName, message, null);
    }

    public AbstractMenuParseException(String fileName, Throwable cause) {
        this(fileName, cause.getMessage(), cause);
    }

    public AbstractMenuParseException(String fileName, String message, Throwable cause) {
        super(message, cause);
        this.fileName = fileName;
    }

}