package ru.soknight.packetinventoryapi.packet.server;

import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.packet.Packet;

public interface ServerPacket extends Packet {

    void send(Player player);

}
