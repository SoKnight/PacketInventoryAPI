package ru.soknight.packetinventoryapi.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PositionedSlot {

    private final int row;
    private final int column;
    private final int rawSlot;
    
}
