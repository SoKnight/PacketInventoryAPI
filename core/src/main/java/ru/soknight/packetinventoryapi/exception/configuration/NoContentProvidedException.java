package ru.soknight.packetinventoryapi.exception.configuration;

public class NoContentProvidedException extends AbstractMenuParseException {

    public NoContentProvidedException(String fileName) {
        super(fileName, "menu '" + fileName + "' doesn't provide any content!");
    }

}
