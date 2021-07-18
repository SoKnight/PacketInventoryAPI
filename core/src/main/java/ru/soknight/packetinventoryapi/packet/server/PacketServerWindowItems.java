package ru.soknight.packetinventoryapi.packet.server;

import com.comphenix.protocol.PacketType;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.nms.ImplementedAs;

import java.util.List;

@ImplementedAs("SimplePacketServerWindowItems")
public interface PacketServerWindowItems extends ServerPacket {

    PacketType PACKET_TYPE = PacketType.Play.Server.WINDOW_ITEMS;

    PacketServerWindowItems windowID(int value);

    PacketServerWindowItems items(List<ItemStack> items);

}
