package ru.soknight.packetinventoryapi.menu.item;

import lombok.Getter;
import ru.soknight.packetinventoryapi.configuration.parse.FillPatternType;
import ru.soknight.packetinventoryapi.nms.vanilla.AbstractVanillaItem;

@Getter
public abstract class AbstractMenuItem<I extends AbstractMenuItem<I, B>, B extends AbstractMenuItem.Builder<I, B>> extends AbstractVanillaItem<I, B> implements MenuItem<I, B> {

    protected int[] slots;
    protected FillPatternType fillPattern;

    public static abstract class Builder<I extends AbstractMenuItem<I, B>, B extends Builder<I, B>> extends AbstractVanillaItem.Builder<I, B> implements MenuItem.Builder<I, B> {
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
