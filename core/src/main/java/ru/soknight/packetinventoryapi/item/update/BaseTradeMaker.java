package ru.soknight.packetinventoryapi.item.update;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

@Getter
@Setter
@Accessors(chain = true)
class BaseTradeMaker implements TradeMaker {

    private final BaseMerchantUpdateRequest request;

    private ItemStack firstItem;
    private ItemStack secondItem;
    private ItemStack outputItem;

    private boolean disabled;
    private int numberOfUses;
    private int limitOfUses;
    private int expGain;

    private float priceMultiplier;

    BaseTradeMaker(BaseMerchantUpdateRequest request) {
        this.request = request;
    }

    @Override
    public MerchantUpdateRequest submitFirst() {
        MerchantRecipe recipe = create();
        return request.add(0, recipe);
    }

    @Override
    public MerchantUpdateRequest submitLast() {
        MerchantRecipe recipe = create();
        return request.add(recipe);
    }

    private MerchantRecipe create() {
        MerchantRecipe recipe = new MerchantRecipe(outputItem, limitOfUses);

        if(firstItem != null)
            recipe.addIngredient(firstItem);
        if(secondItem != null)
            recipe.addIngredient(secondItem);

        recipe.setUses(numberOfUses);
        recipe.setExperienceReward(expGain > 0);
        recipe.setVillagerExperience(expGain);

        recipe.setPriceMultiplier(priceMultiplier);
        return recipe;
    }

}
