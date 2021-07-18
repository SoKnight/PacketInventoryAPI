package ru.soknight.packetinventoryapi.packet.client;

import com.comphenix.protocol.PacketType;
import ru.soknight.packetinventoryapi.nms.ImplementedAs;

@ImplementedAs("SimplePacketClientCloseWindow")
public interface PacketClientCloseWindow extends ClientPacket {

    PacketType PACKET_TYPE = PacketType.Play.Client.CLOSE_WINDOW;

    int getWindowID();

}
