package ru.soknight.packetinventoryapi.packet.server;

import com.comphenix.protocol.PacketType;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.nms.ImplementedAs;

@ImplementedAs("SimplePacketServerSetSlot")
public interface PacketServerSetSlot extends ServerPacket {

    PacketType PACKET_TYPE = PacketType.Play.Server.SET_SLOT;

    PacketServerSetSlot windowID(int value);

    PacketServerSetSlot slot(int value);

    PacketServerSetSlot item(ItemStack value);

}
