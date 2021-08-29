package ru.soknight.packetinventoryapi.menu.item.regular;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.configuration.parse.FillPatternType;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.window.WindowClickListener;
import ru.soknight.packetinventoryapi.menu.item.mapper.ItemMapper;
import ru.soknight.packetinventoryapi.nms.vanilla.AbstractVanillaItem;

@Getter
public abstract class AbstractRegularMenuItem<I extends AbstractRegularMenuItem<I, B>, B extends AbstractRegularMenuItem.Builder<I, B>> extends AbstractVanillaItem<I, B> implements RegularMenuItem<I, B> {

    protected final ConfigurationSection configuration;
    protected int[] slots;
    protected FillPatternType fillPattern;
    protected WindowClickListener<?, ?> clickListener;
    protected ItemMapper itemMapper;

    protected AbstractRegularMenuItem(ConfigurationSection configuration) {
        this.configuration = configuration;
    }

    @Override
    public @NotNull ItemStack asBukkitItemFor(Player viewer, int slot) {
        ItemStack itemStack = asBukkitItem();
        if(itemStack != null && itemMapper != null)
            itemStack = itemMapper.apply(itemStack, viewer, slot);
        return itemStack;
    }

    @SuppressWarnings("unchecked")
    public <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> WindowClickListener<C, R> getClickListener() {
        return (WindowClickListener<C, R>) clickListener;
    }

    @Override
    public I setClickListener(WindowClickListener<?, ?> clickListener) {
        this.clickListener = clickListener;
        return getThis();
    }

    @Override
    public I setItemMapper(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
        return getThis();
    }

    public static abstract class Builder<I extends AbstractRegularMenuItem<I, B>, B extends Builder<I, B>> extends AbstractVanillaItem.Builder<I, B> implements RegularMenuItem.Builder<I, B> {
        protected Builder(I menuItem) {
            super(menuItem);
        }

        @Override
        public B slots(int... value) {
            menuItem.slots = value;
            return getThis();
        }

        @Override
        public B fillPattern(FillPatternType value) {
            menuItem.fillPattern = value;
            return getThis();
        }
    }

}
