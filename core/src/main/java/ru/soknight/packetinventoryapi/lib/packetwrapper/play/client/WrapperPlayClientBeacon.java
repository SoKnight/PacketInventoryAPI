/**
 * This file is part of PacketWrapper.
 * Copyright (C) 2012-2015 Kristian S. Strangeland
 * Copyright (C) 2015 dmulloy2
 *
 * PacketWrapper is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PacketWrapper is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with PacketWrapper.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.soknight.packetinventoryapi.lib.packetwrapper.play.client;

import ru.soknight.packetinventoryapi.lib.packetwrapper.AbstractPacket;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayClientBeacon extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Client.BEACON;
    
    public WrapperPlayClientBeacon() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientBeacon(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve Primary Effect.
     * <p>
     * Notes: a Potion ID. (Was a full Integer for the plugin message)
     * @return The current Primary Effect
     */
    public int getPrimaryEffect() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Retrieve Secondary Effect.
     * <p>
     * Notes: a Potion ID. (Was a full Integer for the plugin message)
     * @return The current Secondary Effect
     */
    public int getSecondaryEffect() {
        return handle.getIntegers().read(1);
    }
    
}
