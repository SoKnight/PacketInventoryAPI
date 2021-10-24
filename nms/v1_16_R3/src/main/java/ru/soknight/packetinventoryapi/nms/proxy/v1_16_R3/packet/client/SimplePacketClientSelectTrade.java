package ru.soknight.packetinventoryapi.nms.proxy.v1_16_R3.packet.client;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.packet.client.PacketClientSelectTrade;
import ru.soknight.packetinventoryapi.packet.client.WrappedClientPacket;

public class SimplePacketClientSelectTrade extends WrappedClientPacket implements PacketClientSelectTrade {

    public SimplePacketClientSelectTrade(PacketContainer container, Player player) {
        super(container, player);
    }

    @Override
    public int getSelectedSlot() {
        return handle.getIntegers().read(0);
    }

}
