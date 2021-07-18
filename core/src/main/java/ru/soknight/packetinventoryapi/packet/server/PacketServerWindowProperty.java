package ru.soknight.packetinventoryapi.packet.server;

import com.comphenix.protocol.PacketType;
import ru.soknight.packetinventoryapi.nms.ImplementedAs;

@ImplementedAs("SimplePacketServerWindowProperty")
public interface PacketServerWindowProperty extends ServerPacket {

    PacketType PACKET_TYPE = PacketType.Play.Server.WINDOW_DATA;

    PacketServerWindowProperty windowID(int value);

    PacketServerWindowProperty property(int value);

    PacketServerWindowProperty value(int value);

}
