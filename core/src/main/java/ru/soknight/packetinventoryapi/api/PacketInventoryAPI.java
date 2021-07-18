package ru.soknight.packetinventoryapi.api;

import ru.soknight.packetinventoryapi.menu.registry.MenuRegistry;
import ru.soknight.packetinventoryapi.PacketInventoryAPIPlugin;
import ru.soknight.packetinventoryapi.storage.ContainerStorage;

public interface PacketInventoryAPI {

    static PacketInventoryAPI getInstance() {
        return PacketInventoryAPIPlugin.getApiInstance();
    }

    ContainerStorage containerStorage();

    MenuRegistry menuRegistry();
    
}
