package ru.soknight.packetinventoryapi.placeholder.container.string;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StringContainer {

    static StringContainer wrap(String string) {
        return new SimpleStringContainer(string);
    }

    @Nullable String getString();

    StringContainer replace(@NotNull String placeholder, @Nullable Object value);

    StringContainer setString(@Nullable String string);

}
