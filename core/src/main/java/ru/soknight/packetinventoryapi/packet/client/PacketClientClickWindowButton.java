package ru.soknight.packetinventoryapi.packet.client;

import com.comphenix.protocol.PacketType;
import ru.soknight.packetinventoryapi.nms.ImplementedAs;

@ImplementedAs("SimplePacketClientClickWindowButton")
public interface PacketClientClickWindowButton extends ClientPacket {

    PacketType PACKET_TYPE = PacketType.Play.Client.ENCHANT_ITEM;

    int getWindowID();

    int getButtonID();

}
