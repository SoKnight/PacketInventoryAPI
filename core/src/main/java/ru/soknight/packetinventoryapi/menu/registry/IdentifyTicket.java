package ru.soknight.packetinventoryapi.menu.registry;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.menu.Menu;

import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
final class IdentifyTicket {

    private final String pluginName;
    private final String menuId;

    static @NotNull IdentifyTicket create(@NotNull Menu<?, ?> menu) {
        return create(menu.getProvidingPlugin().getName(), menu.getName());
    }

    static @NotNull IdentifyTicket create(@NotNull String pluginName, @NotNull String menuId) {
        return new IdentifyTicket(pluginName, menuId);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        IdentifyTicket that = (IdentifyTicket) o;
        return Objects.equals(pluginName, that.pluginName) &&
                Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pluginName, menuId);
    }

    @Override
    public @NotNull String toString() {
        return "IdentifyTicket{" +
                "plugin='" + pluginName + '\'' +
                ", menuId='" + menuId + '\'' +
                '}';
    }

}
