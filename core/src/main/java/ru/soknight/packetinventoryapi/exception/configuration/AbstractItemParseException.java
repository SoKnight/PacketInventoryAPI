package ru.soknight.packetinventoryapi.exception.configuration;

import lombok.Getter;

@Getter
public abstract class AbstractItemParseException extends AbstractMenuParseException {

    private final String itemKey;

    public AbstractItemParseException(String fileName, String itemKey, String message) {
        this(fileName, itemKey, message, null);
    }

    public AbstractItemParseException(String fileName, String itemKey, Throwable cause) {
        this(fileName, itemKey, cause.getMessage(), cause);
    }

    public AbstractItemParseException(String fileName, String itemKey, String message, Throwable cause) {
        super(fileName, "couldn't parse menu item '" + itemKey + "': " + message, cause);
        this.itemKey = itemKey;
    }

}