package ru.soknight.packetinventoryapi.placeholder.container.string;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;

public interface StringContainer {

    static StringContainer wrap(String string, Integer slot) {
        return new SimpleStringContainer(string, slot);
    }

    @Nullable String getString();

    OptionalInt getSlot();

    boolean hasSlot();

    boolean isEmpty();

    boolean contains(String string);

    StringContainer replace(@NotNull String placeholder, @Nullable Object value);

    StringContainer setString(@Nullable String string);

}
