package ru.soknight.packetinventoryapi.packet.client;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import lombok.Getter;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.packet.WrappedPacket;

@Getter
public abstract class WrappedClientPacket extends WrappedPacket implements ClientPacket {

    protected final Player player;

    protected WrappedClientPacket(PacketType packetType, Player player) {
        super(packetType);
        this.player = player;
    }

    protected WrappedClientPacket(PacketContainer handle, Player player) {
        super(handle);
        this.player = player;
    }

}
