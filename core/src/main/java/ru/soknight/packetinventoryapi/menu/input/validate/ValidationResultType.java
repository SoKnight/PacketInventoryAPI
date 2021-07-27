package ru.soknight.packetinventoryapi.menu.input.validate;

public enum ValidationResultType {

    ALLOWED,
    DENIED;

    public static ValidationResultType fromBoolean(boolean hasValidatedSuccessfully) {
        return hasValidatedSuccessfully ? ALLOWED : DENIED;
    }

}
