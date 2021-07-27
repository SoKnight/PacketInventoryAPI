package ru.soknight.packetinventoryapi.placeholder.container.list;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface ListContainer {

    static ListContainer wrap(List<String> list) {
        return new SimpleListContainer(list);
    }

    @Nullable List<String> getList();

    ListContainer loadContentFrom(@NotNull  Collection<String> collection);

    ListContainer replaceAll(@NotNull String placeholder, @Nullable Object value);

}
