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

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.lib.packetwrapper.AbstractPacket;

public class WrapperPlayServerSetSlot extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Server.SET_SLOT;

	public WrapperPlayServerSetSlot() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerSetSlot(PacketContainer packet) {
		super(packet, TYPE);
	}

	// --- window ID
	public int getWindowId() {
		return handle.getIntegers().read(0);
	}
	public WrapperPlayServerSetSlot setWindowId(int value) {
		handle.getIntegers().write(0, value);
		return this;
	}

	// --- target slot
	public int getSlot() {
		return handle.getIntegers().read(1);
	}
	public WrapperPlayServerSetSlot setSlot(int value) {
		handle.getIntegers().write(1, value);
		return this;
	}

	// --- slot data
	public ItemStack getSlotData() {
		return handle.getItemModifier().read(0);
	}
	public WrapperPlayServerSetSlot setSlotData(ItemStack value) {
		handle.getItemModifier().write(0, value);
		return this;
	}

}
