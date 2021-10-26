package ru.soknight.packetinventoryapi.configuration.parse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.exception.configuration.NoMaterialProvidedException;
import ru.soknight.packetinventoryapi.exception.configuration.NoSlotsToDisplayException;
import ru.soknight.packetinventoryapi.exception.configuration.UnknownMaterialException;
import ru.soknight.packetinventoryapi.menu.item.regular.RegularMenuItem;
import ru.soknight.packetinventoryapi.nms.NMSAssistant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public final class ParsedDataRaw {

    public static final ParsedDataRaw DEFAULT = new ParsedDataRaw(null, null);

    private final ConfigurationSection configuration;
    private final String fileName;

    private BaseComponent name;
    private List<String> lore;

    private String materialRaw;
    private Integer amount;
    private int[] slots;

    private String playerHead;
    private String base64Head;
    private String aSkinsHead;
    private Integer customModelData;

    private FillPatternType fillPattern;
    private Boolean enchanted;

    public RegularMenuItem<?, ?> asMenuItem() throws
            NoMaterialProvidedException,
            UnknownMaterialException,
            NoSlotsToDisplayException
    {
        // menu item instance creation
        RegularMenuItem.Builder<?, ?> menuItem = NMSAssistant.newMenuItem(configuration);

        // head textures applying or regular material usage
        if(playerHead != null && !playerHead.isEmpty())
            menuItem.playerHead(playerHead);
        else if(base64Head != null && !base64Head.isEmpty())
            menuItem.base64Head(base64Head);
        else if(aSkinsHead != null && !aSkinsHead.isEmpty())
            menuItem.aSkinsHead(aSkinsHead);
        else {
            if(materialRaw == null || materialRaw.isEmpty())
                throw new NoMaterialProvidedException(fileName, configuration.getName());

            try {
                menuItem.material(parseMaterial());
            } catch (IllegalArgumentException ex) {
                throw new UnknownMaterialException(fileName, configuration.getName(), materialRaw);
            }
        }

        if((slots == null || slots.length == 0) && fillPattern == null)
            throw new NoSlotsToDisplayException(fileName, configuration.getName());

        // setup other values
        return menuItem
                .amount(amount != null ? amount : 1)
                .nameComponent(name)
                .lore(lore)
                .slots(slots)
                .customModelData(customModelData)
                .fillPattern(fillPattern)
                .enchanted(enchanted != null ? enchanted : false)
                .build();
    }

    public ParsedDataRaw duplicate() {
        return duplicate(configuration, fileName);
    }

    public ParsedDataRaw duplicate(ConfigurationSection configuration, String fileName) {
        if(this == DEFAULT)
            return new ParsedDataRaw(configuration, fileName);

        return new ParsedDataRaw(configuration, fileName)
                .setName(name != null ? name.duplicate() : null)
                .setLore(lore != null ? new ArrayList<>(lore) : null)
                .setMaterialRaw(materialRaw)
                .setAmount(amount != null ? amount : 1)
                .setSlots(Arrays.copyOf(slots, slots.length))
                .setASkinsHead(aSkinsHead)
                .setPlayerHead(playerHead)
                .setBase64Head(base64Head)
                .setCustomModelData(customModelData)
                .setFillPattern(fillPattern)
                .setEnchanted(enchanted != null ? enchanted : false);
    }

    public ParsedDataRaw overlapWith(@NotNull ParsedDataRaw other) {
        this.name = overlap(name, other.name);
        this.lore = overlap(lore, other.lore);

        this.materialRaw = overlap(materialRaw, other.materialRaw);
        this.amount = overlap(amount, other.amount);
        this.slots = overlap(slots, other.slots);

        this.aSkinsHead = overlap(aSkinsHead, other.aSkinsHead);
        this.playerHead = overlap(playerHead, other.playerHead);
        this.base64Head = overlap(base64Head, other.base64Head);
        this.customModelData = overlap(customModelData, other.customModelData);

        this.fillPattern = overlap(fillPattern, other.fillPattern);
        this.enchanted = overlap(enchanted, other.enchanted);
        return this;
    }

    private <T> T overlap(T current, T value) {
        return value != null ? value : current;
    }

    public Material parseMaterial() throws NoMaterialProvidedException, UnknownMaterialException {
        if(materialRaw == null || materialRaw.isEmpty())
            throw new NoMaterialProvidedException(fileName, configuration.getName());

        try {
            return Material.valueOf(materialRaw.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new UnknownMaterialException(fileName, configuration.getName(), materialRaw);
        }
    }

    public void validateSlots() throws NoSlotsToDisplayException {
        if((slots == null || slots.length == 0) && fillPattern == null)
            throw new NoSlotsToDisplayException(fileName, configuration.getName());
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        ParsedDataRaw that = (ParsedDataRaw) o;
        return Objects.equals(configuration, that.configuration) &&
                Objects.equals(fileName, that.fileName) &&
                Objects.equals(name, that.name) &&
                Objects.equals(lore, that.lore) &&
                Objects.equals(materialRaw, that.materialRaw) &&
                Objects.equals(amount, that.amount) &&
                Arrays.equals(slots, that.slots) &&
                Objects.equals(aSkinsHead, that.aSkinsHead) &&
                Objects.equals(playerHead, that.playerHead) &&
                Objects.equals(base64Head, that.base64Head) &&
                Objects.equals(customModelData, that.customModelData) &&
                fillPattern == that.fillPattern &&
                Objects.equals(enchanted, that.enchanted);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(
                configuration, fileName, name, lore, materialRaw, amount, aSkinsHead,
                playerHead, base64Head, customModelData, fillPattern, enchanted
        );
        result = 31 * result + Arrays.hashCode(slots);
        return result;
    }

    @Override
    public @NotNull String toString() {
        return "ParsedDataRaw{" +
                "configuration=" + configuration +
                ", fileName='" + fileName + '\'' +
                ", name=" + name +
                ", lore=" + lore +
                ", materialRaw='" + materialRaw + '\'' +
                ", amount=" + amount +
                ", slots=" + Arrays.toString(slots) +
                ", aSkinsHead='" + aSkinsHead + '\'' +
                ", playerHead='" + playerHead + '\'' +
                ", base64Head='" + base64Head + '\'' +
                ", customModelData=" + customModelData +
                ", pattern=" + fillPattern +
                ", enchanted=" + enchanted +
                '}';
    }

}
