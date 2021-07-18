package ru.soknight.packetinventoryapi.configuration.parse;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import ru.soknight.packetinventoryapi.menu.item.MenuItem;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ParsedDataBundle {

    private BaseComponent title;
    private int rowsAmount;
    private MenuItem<?, ?> filler;

    private final Map<String, MenuItem<?, ?>> content;

    public ParsedDataBundle() {
        this.content = new HashMap<>();
    }

    public Map<String, MenuItem<?, ?>> getContent() {
        return Collections.unmodifiableMap(content);
    }

    public void addMenuItem(String id, MenuItem<?, ?> item) {
        content.put(id, item);
    }

    public void addMenuItems(Map<String, MenuItem<?, ?>> items) {
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
