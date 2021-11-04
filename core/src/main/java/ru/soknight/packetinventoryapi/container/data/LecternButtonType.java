package ru.soknight.packetinventoryapi.container.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum LecternButtonType {

    UNKNOWN(-1),
    PREVIOUS_PAGE(1),
    NEXT_PAGE(2),
    TAKE_BOOK(3);

    private final int id;

    public static LecternButtonType getById(int id) {
        return Arrays.stream(values())
                .filter(e -> e.id == id)
                .findFirst().orElse(UNKNOWN);
    }

    @Override
    public String toString() {
        return name().toLowerCase() + " (#" + id + ")";
    }

}
