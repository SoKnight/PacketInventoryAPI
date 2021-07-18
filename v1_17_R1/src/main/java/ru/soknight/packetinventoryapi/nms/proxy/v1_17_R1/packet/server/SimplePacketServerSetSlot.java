package ru.soknight.packetinventoryapi.nms.proxy.v1_17_R1.packet.server;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.packet.server.PacketServerSetSlot;
import ru.soknight.packetinventoryapi.packet.server.WrappedServerPacket;

public class SimplePacketServerSetSlot extends WrappedServerPacket implements PacketServerSetSlot {

    public SimplePacketServerSetSlot() {
        super(PacketServerSetSlot.PACKET_TYPE);
    }

    public SimplePacketServerSetSlot(PacketContainer container) {
        super(container);
    }

    @Override
    public PacketServerSetSlot windowID(int value) {
        handle.getIntegers().write(0, value);
        return this;
    }

    @Override
    public PacketServerSetSlot slot(int value) {
        handle.getIntegers().write(1, value);
        return this;
    }

    @Override
    public PacketServerSetSlot item(ItemStack value) {
        handle.getItemModifier().write(0, value);
        return this;
    }

}
