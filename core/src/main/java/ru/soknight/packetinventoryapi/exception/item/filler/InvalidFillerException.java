package ru.soknight.packetinventoryapi.exception.item.filler;

import lombok.Getter;
import ru.soknight.packetinventoryapi.item.update.content.filler.Filler;

@Getter
public class InvalidFillerException extends RuntimeException {

    protected final Filler filler;

    public InvalidFillerException(Filler filler, String message) {
        super(message);
        this.filler = filler;
    }

}
