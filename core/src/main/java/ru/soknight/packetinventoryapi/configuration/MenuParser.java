package ru.soknight.packetinventoryapi.configuration;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.configuration.parse.FillPatternType;
import ru.soknight.packetinventoryapi.configuration.parse.ParsedDataBundle;
import ru.soknight.packetinventoryapi.exception.configuration.*;
import ru.soknight.packetinventoryapi.menu.item.MenuItem;
import ru.soknight.packetinventoryapi.nms.NMSAssistant;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MenuParser {

    public static final String FILLER_KEY = "$filler";

    public static ParsedDataBundle parse(Plugin plugin, String fileName, Configuration configuration) throws
            InvalidRowsAmountException,
            NoContentProvidedException,
            NoSlotsToDisplayException,
            UnknownFillingPatternException,
            UnknownMaterialException,
            NoMaterialProvidedException
    {
        BaseComponent title = new TextComponent(colorize(configuration.getString("title", "")));

        int rowsAmount = configuration.getInt("rows", 3);
        if(rowsAmount < 1 || rowsAmount > 6)
            throw new InvalidRowsAmountException(fileName, rowsAmount);

        ParsedDataBundle dataBundle = new ParsedDataBundle();
        dataBundle.setTitle(title);
        dataBundle.setRowsAmount(rowsAmount);

        ConfigurationSection content = configuration.getConfigurationSection("content");
        if(content == null)
            throw new NoContentProvidedException(fileName);

        Set<String> keys = content.getKeys(false);
        if(!keys.isEmpty()) {
            Map<String, MenuItem<?, ?>> items = new LinkedHashMap<>();
            for(String key : keys) {
                if(!content.isConfigurationSection(key))
                    continue;

                MenuItem<?, ?> item = parseItem(content.getConfigurationSection(key), fileName);
                items.put(key, item);
            }
            dataBundle.setFiller(items.remove(FILLER_KEY));
            dataBundle.addMenuItems(items);
        }

        return dataBundle;
    }

    public static MenuItem<?, ?> parseItem(ConfigurationSection configuration, String fileName) throws
            NoMaterialProvidedException,
            NoSlotsToDisplayException,
            UnknownMaterialException,
            UnknownFillingPatternException
    {
        // --- gathering some data
        BaseComponent name = new TextComponent(colorize(configuration.getString("name", "")));
        List<String> lore = colorize(configuration.getStringList("lore"));

        String materialRaw = configuration.getString("material");
        int amount = configuration.getInt("amount", 1);
        int[] slots = parseSlots(configuration);

        String playerHead = configuration.getString("player-head");
        String base64Head = configuration.getString("base64-head");
        Integer customModelData = configuration.isInt("custom-model-data") ? configuration.getInt("custom-model-data") : null;

        String patternRaw = configuration.getString("pattern");
        boolean enchanted = configuration.getBoolean("enchanted", false);

        // menu item instance creation
        MenuItem.Builder<?, ?> menuItem = NMSAssistant.newMenuItem();

        // head textures applying or regular material usage
        if(playerHead != null && !playerHead.isEmpty())
            menuItem.playerHead(playerHead);
        else if(base64Head != null && !base64Head.isEmpty())
            menuItem.base64Head(base64Head);
        else {
            if(materialRaw == null || materialRaw.isEmpty())
                throw new NoMaterialProvidedException(fileName, configuration.getName());

            try {
                Material material = Material.valueOf(materialRaw.toUpperCase());
                menuItem.material(material);
            } catch (IllegalArgumentException ex) {
                throw new UnknownMaterialException(fileName, configuration.getName(), materialRaw);
            }
        }

        FillPatternType pattern = null;
        if(patternRaw != null && !patternRaw.isEmpty()) {
            try {
                pattern = FillPatternType.valueOf(patternRaw.toUpperCase());
                menuItem.fillPattern(pattern);
            } catch (IllegalArgumentException ex) {
                throw new UnknownFillingPatternException(fileName, configuration.getName(), patternRaw);
            }
        }

        if(slots.length == 0 && pattern == null)
            throw new NoSlotsToDisplayException(fileName, configuration.getName());

        // setup other values
        menuItem
                .amount(amount)
                .nameComponent(name)
                .lore(lore)
                .slots(slots)
                .customModelData(customModelData)
                .enchanted(enchanted);

        return menuItem.build();
    }

    public static int[] parseSlots(ConfigurationSection configuration) {
        Set<Integer> slots = new LinkedHashSet<>();

        if(configuration.isInt("slot")) {
            int slot = configuration.getInt("slots");
            if(slot >= 0)
                slots.add(slot);
        }

        if(configuration.isList("slots")) {
            List<Integer> list = configuration.getIntegerList("slots");
            list.removeIf(i -> i < 0);
            slots.addAll(list);
        } else if(configuration.isString("slots")) {
            String[] ranges = configuration.getString("slots").split(", ");
            for(String range : ranges) {
                if(range.contains("-")) {
                    String[] bounds = range.split("-");
                    if(bounds.length != 2)
                        continue;

                    Integer firstBound = asInt(bounds[0]);
                    Integer secondBound = asInt(bounds[1]);
                    if(firstBound == null || secondBound == 0)
                        continue;

                    if(firstBound < 0 || secondBound < 0)
                        continue;

                    int min = Math.min(firstBound, secondBound);
                    int max = Math.max(firstBound, secondBound);

                    IntStream.rangeClosed(min, max).forEach(slots::add);
                } else {
                    Integer asInt = asInt(range);
                    if(asInt != null && asInt >= 0)
                        slots.add(asInt);
                }
            }
        }

        return slots.stream().mapToInt(Integer::intValue).toArray();
    }

    private static Integer asInt(String source) {
        try {
            return Integer.parseInt(source);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private static String colorize(String original) {
        return original != null && !original.isEmpty()
                ? ChatColor.translateAlternateColorCodes('&', original)
                : original;
    }

    private static List<String> colorize(List<String> original) {
        return original != null && !original.isEmpty()
                ? original.stream().map(MenuParser::colorizeWithoutCheck).collect(Collectors.toList())
                : original;
    }

    private static String colorizeWithoutCheck(String original) {
        return ChatColor.translateAlternateColorCodes('&', original);
    }

}
