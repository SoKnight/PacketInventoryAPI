package ru.soknight.packetinventoryapi.packet.client;

import com.comphenix.protocol.PacketType;
import ru.soknight.packetinventoryapi.nms.ImplementedAs;

@ImplementedAs("SimplePacketClientSelectTrade")
public interface PacketClientSelectTrade extends ClientPacket {

    PacketType PACKET_TYPE = PacketType.Play.Client.TR_SEL;

    int getSelectedSlot();

}
