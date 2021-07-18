package ru.soknight.packetinventoryapi.menu.registry;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import ru.soknight.packetinventoryapi.menu.Menu;

import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
final class IdentifyTicket {

    private final String pluginName;
    private final String menuName;

    static IdentifyTicket create(Menu<?, ?> menu) {
        return create(menu.getProvidingPlugin().getName(), menu.getName());
    }

    static IdentifyTicket create(String plugin, String menu) {
        return new IdentifyTicket(plugin, menu);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        IdentifyTicket that = (IdentifyTicket) o;
        return Objects.equals(pluginName, that.pluginName) &&
                Objects.equals(menuName, that.menuName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pluginName, menuName);
    }

    @Override
    public String toString() {
        return "IdentifyTicket{" +
                "plugin='" + pluginName + '\'' +
                ", menu='" + menuName + '\'' +
                '}';
    }

}
