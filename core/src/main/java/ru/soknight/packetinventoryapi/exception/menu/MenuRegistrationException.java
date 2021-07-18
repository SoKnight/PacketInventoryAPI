package ru.soknight.packetinventoryapi.exception.menu;

public abstract class MenuRegistrationException extends Exception {

    public MenuRegistrationException(String message) {
        this(message, null);
    }

    public MenuRegistrationException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public MenuRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MenuRegistrationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
