package ru.soknight.packetinventoryapi.exception.item;

import lombok.Getter;

@Getter
public class UnknownItemTypeException extends ItemStackParsingException {

    private static final String FORMAT = "unknown item type '%s'";
    
    private final String itemType;
    
    public UnknownItemTypeException(String itemType) {
        super(String.format(FORMAT, itemType));
        this.itemType = itemType;
    }
    
    public UnknownItemTypeException(String itemType, Throwable throwable) {
        super(String.format(FORMAT, itemType), throwable);
        this.itemType = itemType;
    }

}
