package ru.soknight.packetinventoryapi.menu.item.regular;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.configuration.parse.FillPatternType;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.window.WindowClickListener;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;
import ru.soknight.packetinventoryapi.nms.ImplementedAs;
import ru.soknight.packetinventoryapi.nms.vanilla.VanillaItem;

@ImplementedAs("SimpleRegularMenuItem")
public interface RegularMenuItem<I extends RegularMenuItem<I, B>, B extends RegularMenuItem.Builder<I, B>> extends VanillaItem<I, B>, DisplayableMenuItem {

    @Override
    default @NotNull ItemStack asBukkitItemFor(Player viewer) {
        return asBukkitItem();
    }

    @Override
    default @NotNull RegularMenuItem<?, ?> getItemFor(Player viewer) {
        return this;
    }

    @NotNull int[] getSlots();

    @Nullable FillPatternType getFillPattern();

    <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> WindowClickListener<C, R> getClickListener();

    I setClickListener(WindowClickListener<?, ?> clickListener);

    default boolean isSetAt(int targetSlot) {
        for(int slot : getSlots())
            if(slot == targetSlot)
                return true;

        return false;
    }

    interface Builder<I extends RegularMenuItem<I, B>, B extends Builder<I, B>> extends VanillaItem.Builder<I, B> {
        B slots(int... value);

        B fillPattern(FillPatternType value);
    }

}
