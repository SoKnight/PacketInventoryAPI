package ru.soknight.packetinventoryapi.nms.proxy.v1_18_R2.packet.client;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.packet.client.PacketClientNameItem;
import ru.soknight.packetinventoryapi.packet.client.WrappedClientPacket;

public class SimplePacketClientNameItem extends WrappedClientPacket implements PacketClientNameItem {

    public SimplePacketClientNameItem(PacketContainer container, Player player) {
        super(container, player);
    }

    @Override
    public String getItemName() {
        return handle.getStrings().read(0);
    }

}
