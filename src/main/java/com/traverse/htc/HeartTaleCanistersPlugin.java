package com.traverse.htc;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.entity.LivingEntityInventoryChangeEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.traverse.htc.listeners.HeartAmuletListener;

public class HeartTaleCanistersPlugin extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private HeartAmuletListener heartAmuletListener;

    public HeartTaleCanistersPlugin(JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from %s version %s", this.getName(), this.getManifest().getVersion().toString());
    }

    @Override
    protected void setup() {
        heartAmuletListener = new HeartAmuletListener();
        getEventRegistry().registerGlobal(LivingEntityInventoryChangeEvent.class,
                heartAmuletListener::onInventoryChanged);
    }
}
