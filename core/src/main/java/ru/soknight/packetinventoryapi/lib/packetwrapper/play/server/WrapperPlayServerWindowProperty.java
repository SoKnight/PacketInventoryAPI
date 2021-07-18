/**
 * PacketWrapper - ProtocolLib wrappers for Minecraft packets
 * Copyright (C) dmulloy2 <http://dmulloy2.net>
 * Copyright (C) Kristian S. Strangeland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.soknight.packetinventoryapi.lib.packetwrapper.play.server;

import ru.soknight.packetinventoryapi.lib.packetwrapper.AbstractPacket;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerWindowProperty extends AbstractPacket {
    
	public static final PacketType TYPE = PacketType.Play.Server.WINDOW_DATA;

	public WrapperPlayServerWindowProperty() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerWindowProperty(PacketContainer packet) {
		super(packet, TYPE);
	}

	public int getWindowId() {
		return handle.getIntegers().read(0);
	}
	public WrapperPlayServerWindowProperty setWindowId(int value) {
		handle.getIntegers().write(0, value);
        return this;
	}

	public int getProperty() {
		return handle.getIntegers().read(1);
	}
	public WrapperPlayServerWindowProperty setProperty(int value) {
		handle.getIntegers().write(1, value);
        return this;
	}

	public int getValue() {
		return handle.getIntegers().read(2);
	}
	public WrapperPlayServerWindowProperty setValue(int value) {
		handle.getIntegers().write(2, value);
		return this;
	}

}
