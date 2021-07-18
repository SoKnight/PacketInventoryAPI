package ru.soknight.packetinventoryapi.exception.configuration;

import lombok.Getter;

@Getter
public class UnknownMaterialException extends AbstractItemParseException {

    private final String materialValue;

    public UnknownMaterialException(String fileName, String itemKey, String materialValue) {
        super(fileName, itemKey, "unknown material '" + materialValue + "'!");
        this.materialValue = materialValue;
    }

}
