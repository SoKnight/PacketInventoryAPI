package ru.soknight.packetinventoryapi.nms.proxy.v1_16_R2.packet.client;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.packet.client.PacketClientCloseWindow;
import ru.soknight.packetinventoryapi.packet.client.WrappedClientPacket;

public class SimplePacketClientCloseWindow extends WrappedClientPacket implements PacketClientCloseWindow {

    public SimplePacketClientCloseWindow(PacketContainer container, Player player) {
        super(container, player);
    }

    @Override
    public int getWindowID() {
        return handle.getIntegers().read(0);
    }

}
