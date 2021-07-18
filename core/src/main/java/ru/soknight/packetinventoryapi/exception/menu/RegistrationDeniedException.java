package ru.soknight.packetinventoryapi.exception.menu;

public class RegistrationDeniedException extends MenuRegistrationException {

    public RegistrationDeniedException() {
        super("PacketInventoryAPI can't register a new menu now");
    }

}
