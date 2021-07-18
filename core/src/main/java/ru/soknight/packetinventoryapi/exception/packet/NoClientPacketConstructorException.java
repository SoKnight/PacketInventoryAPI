package ru.soknight.packetinventoryapi.exception.packet;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.packet.client.ClientPacket;

public class NoClientPacketConstructorException extends NoConstructorFoundException {

    public NoClientPacketConstructorException(Class<? extends ClientPacket> packetClass) {
        super(packetClass, PacketContainer.class, Player.class);
    }

}
