package ru.soknight.packetinventoryapi.packet.client;

import com.comphenix.protocol.PacketType;
import org.bukkit.potion.PotionEffectType;
import ru.soknight.packetinventoryapi.nms.ImplementedAs;

@ImplementedAs("SimplePacketClientSetBeaconEffect")
public interface PacketClientSetBeaconEffect extends ClientPacket {

    PacketType PACKET_TYPE = PacketType.Play.Client.BEACON;

    int getPrimaryEffectID();

    @SuppressWarnings("deprecation")
    default PotionEffectType getPrimaryEffect() {
        return PotionEffectType.getById(getPrimaryEffectID());
    }

    int getSecondaryEffectID();

    @SuppressWarnings("deprecation")
    default PotionEffectType getSecondaryEffect() {
        return PotionEffectType.getById(getSecondaryEffectID());
    }

}
