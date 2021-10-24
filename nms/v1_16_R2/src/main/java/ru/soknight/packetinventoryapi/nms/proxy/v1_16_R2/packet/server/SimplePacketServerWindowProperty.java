package ru.soknight.packetinventoryapi.nms.proxy.v1_16_R2.packet.server;

import com.comphenix.protocol.events.PacketContainer;
import ru.soknight.packetinventoryapi.packet.server.PacketServerWindowProperty;
import ru.soknight.packetinventoryapi.packet.server.WrappedServerPacket;

public class SimplePacketServerWindowProperty extends WrappedServerPacket implements PacketServerWindowProperty {

    public SimplePacketServerWindowProperty() {
        super(PacketServerWindowProperty.PACKET_TYPE);
    }

    public SimplePacketServerWindowProperty(PacketContainer container) {
        super(container);
    }

    @Override
    public PacketServerWindowProperty windowID(int value) {
        handle.getIntegers().write(0, value);
        return this;
    }

    @Override
    public PacketServerWindowProperty property(int value) {
        handle.getIntegers().write(1, value);
        return this;
    }

    @Override
    public PacketServerWindowProperty value(int value) {
        handle.getIntegers().write(2, value);
        return this;
    }

}
