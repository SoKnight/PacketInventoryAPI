package ru.soknight.packetinventoryapi.item.update;

import org.bukkit.inventory.MerchantRecipe;
import ru.soknight.packetinventoryapi.container.data.VillagerLevel;
import ru.soknight.packetinventoryapi.container.type.MerchantContainer;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public interface MerchantUpdateRequest {

    static MerchantUpdateRequest create(MerchantContainer container, List<MerchantRecipe> tradeList) {
        return new BaseMerchantUpdateRequest(container, tradeList);
    }

    MerchantContainer getContainer();

    boolean isPushed();

    VillagerLevel getVillagerLevel();
    MerchantUpdateRequest setVillagerLevel(VillagerLevel value);

    int getVillagerExp();
    MerchantUpdateRequest setVillagerExp(int value);

    boolean isWanderingTrader();
    MerchantUpdateRequest setWanderingTrader(boolean value);

    default MerchantUpdateRequest makeWanderingTrader() { return setWanderingTrader(true); }
    default MerchantUpdateRequest makeRegularVillager() { return setWanderingTrader(false); }

    boolean canRestock();
    MerchantUpdateRequest setCanRestock(boolean value);

    default MerchantUpdateRequest allowRestocking() { return setCanRestock(true); }
    default MerchantUpdateRequest denyRestocking() { return setCanRestock(false); }

    List<MerchantRecipe> getTradeList();
    MerchantUpdateRequest resetTradeList();

    MerchantUpdateRequest add(MerchantRecipe recipe);
    MerchantUpdateRequest add(int index, MerchantRecipe recipe);
    MerchantUpdateRequest addAll(Collection<MerchantRecipe> recipes);
    MerchantUpdateRequest addAll(int index, Collection<MerchantRecipe> recipes);

    MerchantUpdateRequest set(int index, MerchantRecipe recipe);

    MerchantUpdateRequest remove(int index);
    MerchantUpdateRequest remove(MerchantRecipe recipe);

    MerchantUpdateRequest removeAll(Collection<MerchantRecipe> recipes);
    MerchantUpdateRequest removeAll(int fromIndex, int toIndex);

    MerchantContainer pushSync();
    CompletableFuture<MerchantContainer> pushAsync();
    CompletableFuture<MerchantContainer> pushLater(long duration, TimeUnit timeUnit);

    TradeMaker createTrade();

}
