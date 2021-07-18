package ru.soknight.packetinventoryapi.container.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum EnchantmentPosition {
    UNKNOWN(-1),
    TOP(0),
    MIDDLE(1),
    BOTTOM(2);

    private final int id;

    public static EnchantmentPosition getById(int id) {
        return Arrays.stream(values())
                .filter(e -> e.id == id)
                .findFirst().orElse(UNKNOWN);
    }

    @Override
    public String toString() {
        return name().toLowerCase() + " (#" + id + ")";
    }
}