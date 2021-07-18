package ru.soknight.packetinventoryapi.exception.item;

public class ItemStackParsingException extends RuntimeException {

    private static final String FORMAT = "itemstack parsing failed: %s";
    
    public ItemStackParsingException(String message) {
        super(String.format(FORMAT, message));
    }
    
    public ItemStackParsingException(Throwable throwable) {
        super(throwable);
    }
    
    public ItemStackParsingException(String message, Throwable throwable) {
        super(String.format(FORMAT, message), throwable, false, false);
    }
    
    protected ItemStackParsingException(
            String message, Throwable throwable,
            boolean suppression, boolean stackTrace
    ) {
        super(String.format(FORMAT, message), throwable, suppression, stackTrace);
    }
    
}
