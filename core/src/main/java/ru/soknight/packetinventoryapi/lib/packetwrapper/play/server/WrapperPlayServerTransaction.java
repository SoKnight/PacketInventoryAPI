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

@SuppressWarnings("deprecation")
public class WrapperPlayServerTransaction extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Server.TRANSACTION;

	public WrapperPlayServerTransaction() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerTransaction(PacketContainer packet) {
		super(packet, TYPE);
	}

	public int getWindowId() {
		return handle.getIntegers().read(0);
	}
	public WrapperPlayServerTransaction setWindowId(int value) {
		handle.getIntegers().write(0, value);
		return this;
	}

	public short getActionNumber() {
		return handle.getShorts().read(0);
	}
	public WrapperPlayServerTransaction setActionNumber(short value) {
		handle.getShorts().write(0, value);
		return this;
	}

	public boolean getAccepted() {
		return handle.getBooleans().read(0);
	}
	public WrapperPlayServerTransaction setAccepted(boolean value) {
		handle.getBooleans().write(0, value);
		return this;
	}

}
