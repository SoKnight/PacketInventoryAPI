package ru.soknight.packetinventoryapi.exception.item.filler;

import lombok.Getter;
import ru.soknight.packetinventoryapi.item.update.content.filler.column.ColumnFiller;

@Getter
public class InvalidColumnFillerException extends RuntimeException {

    protected final ColumnFiller filler;

    public InvalidColumnFillerException(ColumnFiller filler, String message) {
        super(message);
        this.filler = filler;
    }

}
