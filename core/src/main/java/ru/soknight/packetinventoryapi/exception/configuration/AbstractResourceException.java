package ru.soknight.packetinventoryapi.exception.configuration;

public abstract class AbstractResourceException extends Exception {

    public AbstractResourceException(String message, Object... args) {
        this(String.format(message, args), (Throwable) null);
    }

    public AbstractResourceException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public AbstractResourceException(String message, Throwable cause) {
        super(message, cause);
    }

}
