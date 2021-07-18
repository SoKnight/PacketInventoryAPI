package ru.soknight.packetinventoryapi.item.update;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MerchantRecipe;
import ru.soknight.packetinventoryapi.container.data.VillagerLevel;
import ru.soknight.packetinventoryapi.container.type.MerchantContainer;
import ru.soknight.packetinventoryapi.packet.PacketAssistant;
import ru.soknight.packetinventoryapi.packet.server.PacketServerTradeList;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Accessors(chain = true)
class BaseMerchantUpdateRequest implements MerchantUpdateRequest {

    private final MerchantContainer container;
    private final List<MerchantRecipe> tradeList;

    private VillagerLevel villagerLevel;
    private int villagerExp;
    private boolean wanderingTrader;

    @Getter(AccessLevel.NONE)
    private boolean canRestock;

    @Setter(AccessLevel.NONE)
    private boolean pushed;

    BaseMerchantUpdateRequest(MerchantContainer container, List<MerchantRecipe> tradeList) {
        this.container = container;
        this.tradeList = tradeList;

        this.villagerLevel = container.getVillagerLevel();
        this.villagerExp = container.getVillagerExp();
        this.wanderingTrader = container.isWanderingTrader();
        this.canRestock = container.canRestock();
    }

    @Override
    public boolean canRestock() {
        return canRestock;
    }

    @Override
    public MerchantUpdateRequest resetTradeList() {
        tradeList.clear();
        return this;
    }

    @Override
    public MerchantUpdateRequest add(MerchantRecipe recipe) {
        tradeList.add(recipe);
        return this;
    }

    @Override
    public MerchantUpdateRequest add(int index, MerchantRecipe recipe) {
        tradeList.add(index, recipe);
        return this;
    }

    @Override
    public MerchantUpdateRequest addAll(Collection<MerchantRecipe> recipes) {
        tradeList.addAll(recipes);
        return this;
    }

    @Override
    public MerchantUpdateRequest addAll(int index, Collection<MerchantRecipe> recipes) {
        tradeList.addAll(index, recipes);
        return this;
    }

    @Override
    public MerchantUpdateRequest set(int index, MerchantRecipe recipe) {
        tradeList.set(index, recipe);
        return this;
    }

    @Override
    public MerchantUpdateRequest remove(int index) {
        tradeList.remove(index);
        return this;
    }

    @Override
    public MerchantUpdateRequest remove(MerchantRecipe recipe) {
        tradeList.remove(recipe);
        return this;
    }

    @Override
    public MerchantUpdateRequest removeAll(Collection<MerchantRecipe> recipes) {
        tradeList.removeAll(recipes);
        return this;
    }

    @Override
    public MerchantUpdateRequest removeAll(int fromIndex, int toIndex) {
        tradeList.subList(fromIndex, toIndex);
        return this;
    }

    @Override
    public MerchantContainer pushSync() {
        if(!container.isViewing())
            return container;

        Player holder = container.getInventoryHolder();
        if(holder == null || !holder.isOnline()) {
            this.pushed = true;
            return container;
        }

        PacketAssistant.createServerPacket(PacketServerTradeList.class)
                .windowID(container.getInventoryId())
                .trades(tradeList)
                .canRestock(!wanderingTrader && canRestock)
                .experience(wanderingTrader ? 0 : villagerExp)
                .regularVillager(!wanderingTrader)
                .villagerLevel(villagerLevel != null ? villagerLevel.getId() : 0)
                .send(holder);

        this.pushed = true;
        return container;
    }

    @Override
    public CompletableFuture<MerchantContainer> pushAsync() {
        return CompletableFuture.supplyAsync(this::pushSync);
    }

    @Override
    public CompletableFuture<MerchantContainer> pushLater(long duration, TimeUnit timeUnit) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(timeUnit.toMillis(duration));
                return pushSync();
            } catch (InterruptedException ignored) {
                return container;
            }
        });
    }

    @Override
    public TradeMaker createTrade() {
        return new BaseTradeMaker(this);
    }

}
