package ru.soknight.packetinventoryapi.configuration.parse;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;
import ru.soknight.packetinventoryapi.menu.item.MenuItem;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.OptionalInt;

@Getter
@Setter
public final class ParsedDataBundle {

    private BaseComponent title;
    private Integer rowsAmount;
    private DisplayableMenuItem filler;

    private final Map<String, MenuItem> content;

    public ParsedDataBundle() {
        this.content = new LinkedHashMap<>();
    }

    public OptionalInt getRowsAmount() {
        return rowsAmount != null ? OptionalInt.of(rowsAmount) : OptionalInt.empty();
    }

    public Map<String, MenuItem> getContent() {
        return Collections.unmodifiableMap(content);
    }

    public void addMenuItem(String id, MenuItem item) {
        content.put(id, item);
    }

    public void addMenuItems(Map<String, MenuItem> items) {
        content.putAll(items);
    }

    @Override
    public String toString() {
        return "ParsedDataBundle{" +
                "title=" + title +
                ", rowsAmount=" + rowsAmount +
                ", filler=" + filler +
                ", content=" + content +
                '}';
    }

}
