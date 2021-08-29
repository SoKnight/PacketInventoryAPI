package ru.soknight.packetinventoryapi.exception.item.filler;

import lombok.Getter;
import ru.soknight.packetinventoryapi.item.update.content.filler.row.RowFiller;

@Getter
public class InvalidRowFillerException extends RuntimeException {

    protected final RowFiller filler;

    public InvalidRowFillerException(RowFiller filler, String message) {
        super(message);
        this.filler = filler;
    }

}
