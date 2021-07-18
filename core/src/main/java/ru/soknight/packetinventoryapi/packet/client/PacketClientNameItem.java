package ru.soknight.packetinventoryapi.packet.client;

import com.comphenix.protocol.PacketType;
import ru.soknight.packetinventoryapi.nms.ImplementedAs;

@ImplementedAs("SimplePacketClientNameItem")
public interface PacketClientNameItem extends ClientPacket {

    PacketType PACKET_TYPE = PacketType.Play.Client.ITEM_NAME;

    String getItemName();

}
