package ru.soknight.packetinventoryapi.packet.client;

import com.comphenix.protocol.PacketType;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.event.type.WindowClickType;
import ru.soknight.packetinventoryapi.nms.ImplementedAs;

@ImplementedAs("SimplePacketClientClickWindow")
public interface PacketClientClickWindow extends ClientPacket {

    PacketType PACKET_TYPE = PacketType.Play.Client.WINDOW_CLICK;

    int getWindowID();

    int getSlot();

    int getButtonID();

    int getModeID();

    ItemStack getClickedItem();

    default WindowClickType getClickType() {
        return new WindowClickType(getModeID(), getButtonID(), getSlot());
    }

}
