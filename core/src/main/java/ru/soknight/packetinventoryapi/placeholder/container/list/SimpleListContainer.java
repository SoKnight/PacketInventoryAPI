package ru.soknight.packetinventoryapi.placeholder.container.list;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.Collection;
import java.util.List;
import java.util.OptionalInt;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
final class SimpleListContainer implements ListContainer {

    private final List<String> list;
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
        return list == null || list.isEmpty();
    }

    @Override
    public boolean contains(String element) {
        return !isEmpty() && list.contains(element);
    }

    @Override
    public ListContainer loadContentFrom(@NotNull Collection<String> collection) {
        Validate.notNull(collection, "collection");

        synchronized (this) {
            list.clear();
            list.addAll(collection);
            return this;
        }
    }

    @Override
    public ListContainer replaceAll(@NotNull String placeholder, @Nullable Object value) {
        Validate.notNull(placeholder, "placeholder");

        synchronized (this) {
            if(list == null || list.isEmpty())
                return this;

            String asString = value != null ? value.toString() : null;
            list.replaceAll(line -> line.replace(placeholder, asString != null ? asString : "null"));
            return this;
        }
    }

}
