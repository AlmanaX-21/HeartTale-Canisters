package com.traverse.htc.listeners;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.entity.LivingEntityInventoryChangeEvent;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class HeartAmuletListener {

    private static final String HEART_CANISTER_ITEM_ID = "Red_Heart_Canister";
    private static final double HEALTH_PER_CONTAINER = 2.0;

    public void onInventoryChanged(LivingEntityInventoryChangeEvent event) {
        LivingEntity entity = event.getEntity();
        if(!(entity instanceof Player)) return;

        Player player = (Player) entity;
        updatePlayerMaxHealth(player);
    }

    private void updatePlayerMaxHealth(Player player) {
       Inventory inventory = player.getInventory();
        ItemContainer utilitySlots = inventory.getUtility();

        int totalCanisters = 0;
        for(short slot = 0; slot < utilitySlots.getCapacity(); slot++) {
            ItemStack item = utilitySlots.getItemStack(slot);

            if(item != null && isHeartCanister(item)) {
                totalCanisters+=item.getQuantity();

                double bonusHealth = totalCanisters * HEALTH_PER_CONTAINER;


                applyHealthBonus(player, bonusHealth);
            }
        }
    }

    private void applyHealthBonus(Player player, double bonusHealth) {
        Ref<EntityStore> playerRef = player.getReference();
        Store<EntityStore> store = playerRef.getStore();
        EntityStore entityStore = store.getExternalData();
        World world = entityStore.getWorld();

        world.execute(() -> {
            EntityStatMap statMap = (EntityStatMap) store.getComponent(
                    playerRef, EntityStatMap.getComponentType()
            );

            if(statMap != null) {
                int healthStat = DefaultEntityStatTypes.getHealth();
                double currentHealth = statMap.get(healthStat).getIndex();
                double currentMaxHealth = statMap.get(healthStat).getMax();

                double newMaxHealth = currentMaxHealth + bonusHealth;
                statMap.setStatValue(healthStat, (float) newMaxHealth);

                if(currentHealth >= currentMaxHealth) {
                    statMap.setStatValue(healthStat, (float) newMaxHealth);
                }
            }
        });
    }

    private boolean isHeartCanister(ItemStack item) {
        return item.getItemId().equals(HEART_CANISTER_ITEM_ID);
    }
}
