package ru.soknight.packetinventoryapi.packet.client;

import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.packet.Packet;

public interface ClientPacket extends Packet {

    Player getPlayer();

}
