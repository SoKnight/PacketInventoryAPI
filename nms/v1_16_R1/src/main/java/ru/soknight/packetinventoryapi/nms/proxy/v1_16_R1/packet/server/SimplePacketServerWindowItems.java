package ru.soknight.packetinventoryapi.nms.proxy.v1_16_R1.packet.server;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.packet.server.PacketServerWindowItems;
import ru.soknight.packetinventoryapi.packet.server.WrappedServerPacket;

import java.util.List;

public class SimplePacketServerWindowItems extends WrappedServerPacket implements PacketServerWindowItems {

    public SimplePacketServerWindowItems() {
        super(PacketServerWindowItems.PACKET_TYPE);
    }

    public SimplePacketServerWindowItems(PacketContainer container) {
        super(container);
    }

    @Override
    public PacketServerWindowItems windowID(int value) {
        handle.getIntegers().write(0, value);
        return this;
    }

    @Override
    public PacketServerWindowItems items(List<ItemStack> items) {
        handle.getItemListModifier().write(0, items);
        return this;
    }

}
