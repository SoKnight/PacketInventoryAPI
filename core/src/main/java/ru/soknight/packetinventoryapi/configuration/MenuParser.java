package ru.soknight.packetinventoryapi.configuration;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.configuration.item.ConfigurationItemStructure;
import ru.soknight.packetinventoryapi.configuration.item.ConfigurationItemType;
import ru.soknight.packetinventoryapi.configuration.parse.FillPatternType;
import ru.soknight.packetinventoryapi.configuration.parse.ParsedDataBundle;
import ru.soknight.packetinventoryapi.configuration.parse.ParsedDataRaw;
import ru.soknight.packetinventoryapi.exception.configuration.*;
import ru.soknight.packetinventoryapi.menu.item.MenuItem;
import ru.soknight.packetinventoryapi.menu.item.RegularMenuItem;
import ru.soknight.packetinventoryapi.menu.item.StateableMenuItem;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MenuParser {

    public static final String DEFAULT_STATE_KEY = "$default";
    public static final String FILLER_KEY = "$filler";

    public static ParsedDataBundle parse(
            @NotNull Plugin plugin,
            @NotNull String fileName,
            @NotNull Configuration configuration,
            @Nullable ConfigurationItemStructure<?> itemStructure
    ) throws
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
            Map<String, MenuItem> items = new LinkedHashMap<>();
            for(String key : keys) {
                if(!content.isConfigurationSection(key))
                    continue;

                MenuItem item = parseItem(content.getConfigurationSection(key), fileName, itemStructure);
                items.put(key, item);
            }
            dataBundle.setFiller(items.remove(FILLER_KEY));
            dataBundle.addMenuItems(items);
        }

        return dataBundle;
    }

    public static MenuItem parseItem(
            @NotNull ConfigurationSection configuration,
            @NotNull String fileName,
            @Nullable ConfigurationItemStructure<?> itemStructure
    ) throws
            NoMaterialProvidedException,
            NoSlotsToDisplayException,
            UnknownMaterialException,
            UnknownFillingPatternException
    {
        // --- determining item type
        String id = configuration.getName();
        ConfigurationItemType itemType = itemStructure != null
                ? itemStructure.getItemType(id)
                : ConfigurationItemType.REGULAR;

        // --- parsing item from configuration
        switch (itemType) {
            case REGULAR:
                return parseRegularItem(configuration, fileName);
            case STATEABLE:
                return parseStateableItem(configuration, fileName);
            default:
                throw new IllegalStateException("unexpected item type: " + itemType);
        }
    }

    public static RegularMenuItem<?, ?> parseRegularItem(
            @NotNull ConfigurationSection configuration,
            @NotNull String fileName
    ) throws
            NoMaterialProvidedException,
            UnknownMaterialException,
            UnknownFillingPatternException,
            NoSlotsToDisplayException
    {
        // parsing raw data
        ParsedDataRaw parsedDataRaw = parseDataRaw(configuration, fileName);

        // validating overlapped data bundle
        parsedDataRaw.validateSlots();

        return parsedDataRaw.asMenuItem();
    }

    public static StateableMenuItem parseStateableItem(
            @NotNull ConfigurationSection configuration,
            @NotNull String fileName
    ) throws
            NoMaterialProvidedException,
            UnknownMaterialException,
            UnknownFillingPatternException,
            NoSlotsToDisplayException
    {
        StateableMenuItem.Builder stateableItem = StateableMenuItem.createNew();

        ConfigurationSection defaultConfiguration = configuration.getConfigurationSection(DEFAULT_STATE_KEY);
        ParsedDataRaw defaultDataRaw = parseDataRaw(defaultConfiguration, fileName);

        Set<String> keys = configuration.getKeys(false);
        for(String key : keys) {
            if(key.equals(DEFAULT_STATE_KEY) || !configuration.isConfigurationSection(key))
                continue;

            ConfigurationSection section = configuration.getConfigurationSection(key);
            RegularMenuItem<?, ?> regularItem = parseStateItem(section, fileName, defaultDataRaw);
            stateableItem.addStateItem(key, regularItem);
        }

        return stateableItem.build();
    }

    public static @NotNull RegularMenuItem<?, ?> parseStateItem(
            @NotNull ConfigurationSection configuration,
            @NotNull String fileName,
            @NotNull ParsedDataRaw defaultData
    ) throws
            NoMaterialProvidedException,
            UnknownMaterialException,
            UnknownFillingPatternException,
            NoSlotsToDisplayException
    {
        // parsing raw data and overlapping the default data with that
        ParsedDataRaw parsedDataRaw = parseDataRaw(configuration, fileName);
        ParsedDataRaw overlapped = defaultData.duplicate(configuration, fileName).overlapWith(parsedDataRaw);

        // validating overlapped data bundle
        overlapped.validateSlots();

        return overlapped.asMenuItem();
    }

    private static ParsedDataRaw parseDataRaw(
            @Nullable ConfigurationSection configuration,
            @NotNull String fileName
    ) throws UnknownFillingPatternException {
        if(configuration == null)
            return ParsedDataRaw.DEFAULT.duplicate(configuration, fileName);

        // --- gathering some data
        String nameRaw = colorize(configuration.getString("name"));
        BaseComponent name = nameRaw != null ? new TextComponent(nameRaw) : null;
        List<String> lore = colorize(configuration.getStringList("lore"));

        String materialRaw = configuration.getString("material");
        Integer amount = configuration.isInt("amount") ? configuration.getInt("amount") : null;
        int[] slots = parseSlots(configuration);

        String playerHead = configuration.getString("player-head");
        String base64Head = configuration.getString("base64-head");
        Integer customModelData = configuration.isInt("custom-model-data") ? configuration.getInt("custom-model-data") : null;

        String patternRaw = configuration.getString("pattern");
        Boolean enchanted = configuration.isBoolean("enchanted") ? configuration.getBoolean("enchanted") : null;

        // -- parsing fill pattern type
        FillPatternType pattern = null;
        if(patternRaw != null && !patternRaw.isEmpty()) {
            try {
                pattern = FillPatternType.valueOf(patternRaw.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new UnknownFillingPatternException(fileName, configuration.getName(), patternRaw);
            }
        }

        return new ParsedDataRaw(configuration, fileName)
                .setName(name)
                .setLore(lore)
                .setMaterialRaw(materialRaw)
                .setAmount(amount)
                .setSlots(slots.length > 0 ? slots : null)
                .setPlayerHead(playerHead)
                .setBase64Head(base64Head)
                .setCustomModelData(customModelData)
                .setPattern(pattern)
                .setEnchanted(enchanted);
    }

    public static int[] parseSlots(@NotNull ConfigurationSection configuration) {
        Set<Integer> slots = new LinkedHashSet<>();

        if(configuration.isInt("slot")) {
            int slot = configuration.getInt("slot");
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

        return slots.stream()
                .mapToInt(Integer::intValue)
                .toArray();
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
