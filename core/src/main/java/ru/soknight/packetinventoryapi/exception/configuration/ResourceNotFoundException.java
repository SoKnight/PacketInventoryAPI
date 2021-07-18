package ru.soknight.packetinventoryapi.exception.configuration;

public class ResourceNotFoundException extends AbstractResourceException {

    public ResourceNotFoundException(String message, Object... args) {
        super(message, args);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
