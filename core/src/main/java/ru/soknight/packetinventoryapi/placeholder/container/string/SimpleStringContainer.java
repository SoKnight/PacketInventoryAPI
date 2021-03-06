package ru.soknight.packetinventoryapi.placeholder.container.string;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.OptionalInt;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
final class SimpleStringContainer implements StringContainer {

    private String string;
    private final Integer slot;

    @Override
    public OptionalInt getSlot() {
        return slot != null ? OptionalInt.of(slot) : OptionalInt.empty();
    }

    @Override
    public boolean hasSlot() {
        return slot != null;
    }

    @Override
    public boolean isEmpty() {
        return string == null || string.isEmpty();
    }

    @Override
    public boolean contains(String string) {
        return !isEmpty() && string.contains(string);
    }

    @Override
    public StringContainer setString(String string) {
        synchronized (this) {
            this.string = string;
            return this;
        }
    }

    @Override
    public StringContainer replace(@NotNull String placeholder, @Nullable Object value) {
        Validate.notNull(placeholder, "placeholder");

        synchronized (this) {
            if(string == null || string.isEmpty())
                return this;

            String asString = value != null ? value.toString() : null;
            string = string.replace(placeholder, asString != null ? asString : "null");
            return this;
        }
    }

}
