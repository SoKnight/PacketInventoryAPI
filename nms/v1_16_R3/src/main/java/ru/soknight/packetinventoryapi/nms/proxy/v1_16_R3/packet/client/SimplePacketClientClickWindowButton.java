package ru.soknight.packetinventoryapi.nms.proxy.v1_16_R3.packet.client;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.packet.client.PacketClientClickWindowButton;
import ru.soknight.packetinventoryapi.packet.client.WrappedClientPacket;

public class SimplePacketClientClickWindowButton extends WrappedClientPacket implements PacketClientClickWindowButton {

    public SimplePacketClientClickWindowButton(PacketContainer container, Player player) {
        super(container, player);
    }

    @Override
    public int getWindowID() {
        return handle.getIntegers().read(0);
    }

    @Override
    public int getButtonID() {
        return handle.getIntegers().read(1);
    }

}
