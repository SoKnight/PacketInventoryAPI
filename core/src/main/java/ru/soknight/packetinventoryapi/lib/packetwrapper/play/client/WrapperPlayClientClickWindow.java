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
package ru.soknight.packetinventoryapi.lib.packetwrapper.play.client;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.bukkit.inventory.ItemStack;

import ru.soknight.packetinventoryapi.lib.packetwrapper.AbstractPacket;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

import lombok.Getter;

public class WrapperPlayClientClickWindow extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Client.WINDOW_CLICK;

	public WrapperPlayClientClickWindow() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayClientClickWindow(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Window ID.
	 * <br>
	 * Notes: the id of the window which was clicked. 0 for player inventory.
	 * 
	 * @return The current Window ID
	 */
	public int getWindowId() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Retrieve Slot.
	 * <br>
	 * Notes: the clicked slot.
	 * 
	 * @return The current Slot
	 */
	public int getSlot() {
		return handle.getIntegers().read(1);
	}

	/**
	 * Retrieve Button.
	 * <br>
	 * Notes: the button used in the click.
	 * 
	 * @return The current Button
	 */
	public int getButton() {
		return handle.getIntegers().read(2);
	}

	/**
	 * Retrieve Action number.
	 * <br>
	 * Notes: a unique number for the action, used for transaction handling.
	 * 
	 * @return The current Action number
	 */
	public short getActionNumber() {
		return handle.getShorts().read(0);
	}

	/**
	 * Retrieve Clicked item.
	 * 
	 * @return The current Clicked item
	 */
	public ItemStack getClickedItem() {
		return handle.getItemModifier().read(0);
	}
	
	public int getMode() {
        return handle.getEnumModifier(InventoryClickType.class, 3).read(0).getId();
    }

    @Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum InventoryClickType {
        PICKUP(0),
        QUICK_MOVE(1),
        SWAP(2),
        CLONE(3),
        THROW(4),
        QUICK_CRAFT(5),
        PICKUP_ALL(6);
        
        private final int id;
    }
	
}
