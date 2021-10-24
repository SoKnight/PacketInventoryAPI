package ru.soknight.packetinventoryapi.nms.proxy.v1_17_R1.packet.client;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.packet.client.PacketClientSetBeaconEffect;
import ru.soknight.packetinventoryapi.packet.client.WrappedClientPacket;

public class SimplePacketClientSetBeaconEffect extends WrappedClientPacket implements PacketClientSetBeaconEffect {

    public SimplePacketClientSetBeaconEffect(PacketContainer container, Player player) {
        super(container, player);
    }

    @Override
    public int getPrimaryEffectID() {
        return handle.getIntegers().read(0);
    }

    @Override
    public int getSecondaryEffectID() {
        return handle.getIntegers().read(1);
    }

}
