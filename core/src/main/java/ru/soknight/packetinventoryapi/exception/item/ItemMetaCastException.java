package ru.soknight.packetinventoryapi.exception.item;

import lombok.Getter;

@Getter
public class ItemMetaCastException extends ItemStackParsingException {
    
    private static final String FORMAT = "ItemMeta cannot be cast to '%s'";

    private final Class<?> itemMetaClass;
    
    public ItemMetaCastException(Class<?> itemMetaClass) {
        super(String.format(FORMAT, itemMetaClass.getSimpleName()));
        this.itemMetaClass = itemMetaClass;
    }
    
    public ItemMetaCastException(Class<?> itemMetaClass, Throwable throwable) {
        super(String.format(FORMAT, itemMetaClass.getSimpleName()), throwable);
        this.itemMetaClass = itemMetaClass;
    }

}
