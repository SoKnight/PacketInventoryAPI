package ru.soknight.packetinventoryapi.exception.item;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;

@Getter
public class ItemInsertFailedException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "failed to insert menu item '%s': %s";

    private final DisplayableMenuItem menuItem;

    public ItemInsertFailedException(@NotNull DisplayableMenuItem menuItem) {
        this("unknown reason", null, menuItem);
    }

    public ItemInsertFailedException(@NotNull String message, @NotNull DisplayableMenuItem menuItem) {
        this(message, null, menuItem);
    }

    public ItemInsertFailedException(@Nullable Throwable cause, @NotNull DisplayableMenuItem menuItem) {
        this(cause.getMessage(), cause, menuItem);
    }

    public ItemInsertFailedException(@NotNull String message, @Nullable Throwable cause, @NotNull DisplayableMenuItem menuItem) {
        super(String.format(MESSAGE_FORMAT, menuItem.getConfiguration().getCurrentPath(), message), cause);
        this.menuItem = menuItem;
    }

}
