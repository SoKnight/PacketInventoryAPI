package ru.soknight.packetinventoryapi.item.update;

import org.bukkit.inventory.ItemStack;

public interface TradeMaker {

    MerchantUpdateRequest submitFirst();
    MerchantUpdateRequest submitLast();

    ItemStack getFirstItem();
    TradeMaker setFirstItem(ItemStack item);

    ItemStack getSecondItem();
    TradeMaker setSecondItem(ItemStack item);

    ItemStack getOutputItem();
    TradeMaker setOutputItem(ItemStack item);

    boolean isDisabled();
    TradeMaker setDisabled(boolean value);

    default TradeMaker enable() { return setDisabled(false); }
    default TradeMaker disable() { return setDisabled(true); }

    int getNumberOfUses();
    TradeMaker setNumberOfUses(int value);

    int getLimitOfUses();
    TradeMaker setLimitOfUses(int value);

    int getExpGain();
    TradeMaker setExpGain(int value);

    float getPriceMultiplier();
    TradeMaker setPriceMultiplier(float value);

}
