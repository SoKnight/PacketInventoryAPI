package ru.soknight.packetinventoryapi.exception.configuration;

import lombok.Getter;

@Getter
public class InvalidRowsAmountException extends AbstractMenuParseException {

    private final int rowsAmount;

    public InvalidRowsAmountException(String fileName, int rowsAmount) {
        super(fileName, "rows amount value cannot equal " + rowsAmount + ", it must be from 1 to 6 inclusive!");
        this.rowsAmount = rowsAmount;
    }

}
