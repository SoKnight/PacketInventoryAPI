package ru.soknight.packetinventoryapi.plugin.command;

import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ru.soknight.packetinventoryapi.animation.Animation;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.data.Property;
import ru.soknight.packetinventoryapi.container.type.BrewingStandContainer;
import ru.soknight.packetinventoryapi.container.type.FurnaceContainer;
import ru.soknight.packetinventoryapi.container.type.HopperContainer;
import ru.soknight.packetinventoryapi.plugin.menu.ExampleMenu;

public class CommandDebug implements CommandExecutor {

    private final ExampleMenu menu;

    @SneakyThrows
    public CommandDebug(JavaPlugin plugin) {
        this.menu = new ExampleMenu(plugin);
        this.menu.register();

        plugin.getCommand("packetinvdebug").setExecutor(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))
            return true;
        
        Player player = (Player) sender;
        menu.open(player);
        return true;
    }

    private static final class HopperLoading extends Animation<HopperLoading> {

        private static final ItemStack ITEM = new ItemStack(Material.LIME_STAINED_GLASS);

        public HopperLoading(HopperContainer container) {
            super(container, 5, -1);
        }

        @Override
        protected HopperLoading getThis() {
            return this;
        }

        @Override
        protected void tick(int cycle, int step) throws InterruptedException {
            boolean forward = cycle % 2 == 0;

            if(step != 0) {
                int slot = forward ? step : 4 - step;
                container.updateContent()
                        .removeAll(HopperContainer.hopperSlots())
                        .set(ITEM, slot, true)
                        .pushSync();
            }

            Thread.sleep(100L);
        }

    }

    private static final class Brewing extends Animation<Brewing> {

        public Brewing(BrewingStandContainer container) {
            super(container, 81, -1);
        }

        @Override
        protected Brewing getThis() {
            return this;
        }

        @Override
        protected void tick(int cycle, int step) throws InterruptedException {
            container.updateProperty(Property.BrewingStand.BREW_TIME, (80 - step) * 5);
            Thread.sleep(100L);
        }

    }

    private static final class Loading extends Animation<Loading> {

        public Loading(FurnaceContainer<?> container) {
            super(container, 101, -1);
        }

        @Override
        protected Loading getThis() {
            return this;
        }

        @Override
        protected void tick(int cycle, int step) throws InterruptedException {
            container.updateProperty(Property.Furnace.CURRENT_PROGRESS, step);
            Thread.sleep(20L);
        }

    }

    private static final class Rainbow extends Animation<Rainbow> {

        private static final ItemStack RED = new ItemStack(Material.RED_STAINED_GLASS);
        private static final ItemStack ORANGE = new ItemStack(Material.ORANGE_STAINED_GLASS);
        private static final ItemStack YELLOW = new ItemStack(Material.YELLOW_STAINED_GLASS);
        private static final ItemStack LIME = new ItemStack(Material.LIME_STAINED_GLASS);
        private static final ItemStack LIGHT_BLUE = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS);
        private static final ItemStack BLUE = new ItemStack(Material.BLUE_STAINED_GLASS);
        private static final ItemStack PURPLE = new ItemStack(Material.PURPLE_STAINED_GLASS);

        public Rainbow(Container<?, ?> container) {
            super(container, 7, -1);
        }

        @Override
        protected Rainbow getThis() {
            return this;
        }

        @Override
        protected void tick(int cycle, int step) throws InterruptedException {
            container.updateContent()
                    .column(RED, 1 + circleIndex(step, 0), true)
                    .column(ORANGE, 1 + circleIndex(step, 1), true)
                    .column(YELLOW, 1 + circleIndex(step, 2), true)
                    .column(LIME, 1 + circleIndex(step, 3), true)
                    .column(LIGHT_BLUE, 1 + circleIndex(step, 4), true)
                    .column(BLUE, 1 + circleIndex(step, 5), true)
                    .column(PURPLE, 1 + circleIndex(step, 6), true)
                    .pushSync();

            Thread.sleep(100);
        }

        private int circleIndex(int step, int index) {
            int result = index - step - 1;
            return result >= 0 ? result : result + 7;
        }

    }

}
