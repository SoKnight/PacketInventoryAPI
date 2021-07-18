package ru.soknight.packetinventoryapi.configuration.parse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;

import java.util.function.BiConsumer;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum FillPatternType {

    ALL((i, r) -> r.fillAll(i, false)),
    TOP((i, r) -> r.fillTop(i, false)),
    BOTTOM((i, r) -> r.fillBottom(i, false)),
    INVENTORY((i, r) -> r.fillPlayerInventory(i, false)),
    HOTBAR((i, r) -> r.fillPlayerHotbar(i, false));

    private final BiConsumer<ItemStack, ContentUpdateRequest<?, ?>> filler;

    public void fill(ItemStack bukkitItem, ContentUpdateRequest<?, ?> updateRequest) {
        filler.accept(bukkitItem, updateRequest);
    }

}
