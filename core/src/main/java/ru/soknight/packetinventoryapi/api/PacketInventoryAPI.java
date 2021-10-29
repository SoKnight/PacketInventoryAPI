package ru.soknight.packetinventoryapi.api;

import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.integration.skins.SkinsProvidingBus;
import ru.soknight.packetinventoryapi.menu.registry.MenuRegistry;
import ru.soknight.packetinventoryapi.PacketInventoryAPIPlugin;
import ru.soknight.packetinventoryapi.storage.ContainerStorage;

public interface PacketInventoryAPI {

    static @NotNull PacketInventoryAPI getInstance() {
        return PacketInventoryAPIPlugin.getApiInstance();
    }

    @NotNull ContainerStorage containerStorage();

    @NotNull MenuRegistry menuRegistry();

    @NotNull SkinsProvidingBus skinsProvidingBus();

}
