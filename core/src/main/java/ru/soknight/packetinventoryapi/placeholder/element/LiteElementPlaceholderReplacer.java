package ru.soknight.packetinventoryapi.placeholder.element;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.placeholder.container.list.ListContainer;
import ru.soknight.packetinventoryapi.placeholder.container.string.StringContainer;

import java.util.List;

@FunctionalInterface
public interface LiteElementPlaceholderReplacer extends ElementPlaceholderReplacer {

    @Override
    default void replace(@NotNull Player player, @NotNull ListContainer container, int slot, int pageIndex, int totalIndex) {
        List<String> list = container.getList();
        if(list != null && !list.isEmpty()) {
            list.replaceAll(line -> {
                StringContainer wrapper = StringContainer.wrap(line);
                replace(player, wrapper, slot, pageIndex, totalIndex);
                return wrapper.getString();
            });
        }
    }

}
