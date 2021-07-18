package ru.soknight.packetinventoryapi.exception.configuration;

public class ResourceCopyException extends AbstractResourceException {

    public ResourceCopyException(String message, Object... args) {
        super(message, args);
    }

    public ResourceCopyException(Throwable cause) {
        super(cause);
    }

    public ResourceCopyException(String message, Throwable cause) {
        super(message, cause);
    }

}
