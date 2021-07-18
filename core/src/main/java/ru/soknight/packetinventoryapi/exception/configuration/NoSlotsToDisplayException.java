package ru.soknight.packetinventoryapi.exception.configuration;

public class NoSlotsToDisplayException extends AbstractItemParseException {

    public NoSlotsToDisplayException(String fileName, String itemKey) {
        super(fileName, itemKey, "item has no slots where may be displayed!");
    }

}
