package ru.soknight.packetinventoryapi.configuration.parse;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;
import ru.soknight.packetinventoryapi.menu.item.MenuItem;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.OptionalInt;

@Getter
@Setter
public final class ParsedDataBundle {

    private @NotNull BaseComponent title;
    private @Nullable Integer rowsAmount;
    private @Nullable DisplayableMenuItem filler;

    private final Map<String, MenuItem> content;

    public ParsedDataBundle() {
        this.content = new LinkedHashMap<>();
    }

    public @NotNull OptionalInt getRowsAmount() {
        return rowsAmount != null ? OptionalInt.of(rowsAmount) : OptionalInt.empty();
    }

    public @NotNull @UnmodifiableView Map<String, MenuItem> getContent() {
        return Collections.unmodifiableMap(content);
    }

    public void addMenuItem(@NotNull String id, @NotNull MenuItem item) {
        content.put(id, item);
    }

    public void addMenuItems(@NotNull Map<String, MenuItem> items) {
        content.putAll(items);
    }

    @Override
    public @NotNull String toString() {
        return "ParsedDataBundle{" +
                "title=" + title +
                ", rowsAmount=" + rowsAmount +
                ", filler=" + filler +
                ", content=" + content +
                '}';
    }

}
