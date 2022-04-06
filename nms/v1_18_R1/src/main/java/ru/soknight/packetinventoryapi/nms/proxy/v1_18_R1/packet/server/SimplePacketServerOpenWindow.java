package ru.soknight.packetinventoryapi.nms.proxy.v1_18_R1.packet.server;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.ComponentConverter;
import net.md_5.bungee.api.chat.BaseComponent;
import ru.soknight.packetinventoryapi.packet.server.PacketServerOpenWindow;
import ru.soknight.packetinventoryapi.packet.server.WrappedServerPacket;

public class SimplePacketServerOpenWindow extends WrappedServerPacket implements PacketServerOpenWindow {

    public SimplePacketServerOpenWindow() {
        super(PacketServerOpenWindow.PACKET_TYPE);
    }

    public SimplePacketServerOpenWindow(PacketContainer container) {
        super(container);
    }

    @Override
    public PacketServerOpenWindow windowID(int value) {
        handle.getIntegers().write(0, value);
        return this;
    }

    @Override
    public PacketServerOpenWindow windowType(int value) {
        handle.getIntegers().write(1, value);
        return this;
    }

    @Override
    public PacketServerOpenWindow windowTitle(BaseComponent value) {
        handle.getChatComponents().write(0, ComponentConverter.fromBaseComponent(value));
        return this;
    }

}
