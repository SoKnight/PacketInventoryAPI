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
package ru.soknight.packetinventoryapi.lib.packetwrapper.play.server;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.inventory.MerchantRecipe;
import ru.soknight.packetinventoryapi.lib.packetwrapper.AbstractPacket;

import java.util.List;

public class WrapperPlayServerTradeList extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Server.OPEN_WINDOW_MERCHANT;
    
    public WrapperPlayServerTradeList() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerTradeList(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    public int getWindowId() {
        return handle.getIntegers().read(0);
    }
    public WrapperPlayServerTradeList setWindowId(int value) {
        handle.getIntegers().write(0, value);
        return this;
    }
    
    public List<MerchantRecipe> getTrades() {
        return handle.getMerchantRecipeLists().read(0);
    }
    public WrapperPlayServerTradeList setTrades(List<MerchantRecipe> trades) {
        handle.getMerchantRecipeLists().write(0, trades);
        return this;
    }
    
    public int getVillagerLevel() {
        return handle.getIntegers().read(1);
    }
    public WrapperPlayServerTradeList setVillagerLevel(int value) {
        handle.getIntegers().write(1, value);
        return this;
    }
    
    public int getExperience() {
        return handle.getIntegers().read(2);
    }
    public WrapperPlayServerTradeList setExperience(int value) {
        handle.getIntegers().write(2, value);
        return this;
    }
    
    public boolean isRegularVillager() {
        return handle.getBooleans().read(0);
    }
    public WrapperPlayServerTradeList setRegularVillager(boolean value) {
        handle.getBooleans().write(0, value);
        return this;
    }
    
    public boolean canRestock() {
        return handle.getBooleans().read(1);
    }
    public WrapperPlayServerTradeList setCanRestock(boolean value) {
        handle.getBooleans().write(1, value);
        return this;
    }
    
}
