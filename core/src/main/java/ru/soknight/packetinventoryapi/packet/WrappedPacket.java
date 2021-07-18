package ru.soknight.packetinventoryapi.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

@Getter
public abstract class WrappedPacket implements Packet {

    protected final PacketContainer handle;

    protected WrappedPacket(PacketType packetType) {
        this.handle = new PacketContainer(packetType);
    }

    protected WrappedPacket(PacketContainer handle) {
        this.handle = handle;
    }

    @Override
    public String toString() {
        List<Field> fields = handle.getModifier().getFields();
        List<Object> values = handle.getModifier().getValues();

        SortedMap<String, Object> pairs = new TreeMap<>(Comparator.naturalOrder());
        for(int i = 0; i < fields.size(); i++)
            pairs.put("'[" + fields.get(i).getType().getSimpleName() + "] " + fields.get(i).getName() + "'", values.get(i));

        return "Packet{" +
                "fields=" + pairs +
                '}';
    }

}
