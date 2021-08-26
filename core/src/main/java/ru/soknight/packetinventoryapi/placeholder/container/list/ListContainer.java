package ru.soknight.packetinventoryapi.placeholder.container.list;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.OptionalInt;

public interface ListContainer {

    static ListContainer wrap(List<String> list, int slot) {
        return new SimpleListContainer(list, slot);
    }

    @Nullable List<String> getList();

    OptionalInt getSlot();

    boolean hasSlot();

    boolean isEmpty();

    boolean contains(String element);

    ListContainer loadContentFrom(@NotNull  Collection<String> collection);

    ListContainer replaceAll(@NotNull String placeholder, @Nullable Object value);

}
