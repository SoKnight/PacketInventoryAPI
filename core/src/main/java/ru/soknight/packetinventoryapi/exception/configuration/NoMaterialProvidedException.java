package ru.soknight.packetinventoryapi.exception.configuration;

public class NoMaterialProvidedException extends AbstractItemParseException {

    public NoMaterialProvidedException(String fileName, String itemKey) {
        super(fileName, itemKey, "item has no material!");
    }

}
