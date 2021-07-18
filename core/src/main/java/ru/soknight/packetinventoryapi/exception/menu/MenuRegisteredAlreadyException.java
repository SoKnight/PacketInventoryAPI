package ru.soknight.packetinventoryapi.exception.menu;

import ru.soknight.packetinventoryapi.menu.Menu;

public class MenuRegisteredAlreadyException extends MenuRegistrationException {

    private static final String MESSAGE_FORMAT = "menu '%s' is already registered!";

    public MenuRegisteredAlreadyException(Menu<?, ?> menu) {
        super(String.format(MESSAGE_FORMAT, menu.getName()));
    }

}
