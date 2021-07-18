package ru.soknight.packetinventoryapi.nms.proxy.v1_15_R1.packet.server;

import com.comphenix.protocol.events.PacketContainer;
import ru.soknight.packetinventoryapi.packet.server.PacketServerCloseWindow;
import ru.soknight.packetinventoryapi.packet.server.WrappedServerPacket;

public class SimplePacketServerCloseWindow extends WrappedServerPacket implements PacketServerCloseWindow {

    public SimplePacketServerCloseWindow() {
        super(PacketServerCloseWindow.PACKET_TYPE);
    }

    public SimplePacketServerCloseWindow(PacketContainer container) {
        super(container);
    }

    @Override
    public PacketServerCloseWindow windowID(int value) {
        handle.getIntegers().write(0, value);
        return this;
    }

}
