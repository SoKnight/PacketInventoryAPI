package ru.soknight.packetinventoryapi.packet.server;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.packet.WrappedPacket;

public abstract class WrappedServerPacket extends WrappedPacket implements ServerPacket {

    protected WrappedServerPacket(PacketType packetType) {
        super(packetType);
    }

    protected WrappedServerPacket(PacketContainer handle) {
        super(handle);
    }

    @Override
    @SneakyThrows
    public void send(Player player) {
        if(player.isOnline())
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, handle);
    }

}
