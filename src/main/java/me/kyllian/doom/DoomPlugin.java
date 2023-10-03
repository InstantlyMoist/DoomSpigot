package me.kyllian.doom;

import lombok.Getter;
import me.kyllian.doom.commands.DoomExecutor;
import me.kyllian.doom.data.Pocket;
import me.kyllian.doom.handlers.MessageHandler;
import me.kyllian.doom.handlers.PlayerHandler;
import me.kyllian.doom.handlers.map.MapHandler;
import me.kyllian.doom.handlers.map.MapHandlerFactory;
import me.kyllian.doom.listeners.*;
import me.kyllian.doom.listeners.packets.SteerVehicleListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class DoomPlugin extends JavaPlugin {

    private boolean protocolLib;

    private MapHandler mapHandler;
    private MessageHandler messageHandler;
    private PlayerHandler playerHandler;

    @Override
    public void onEnable() {
        super.onEnable();

        protocolLib = Bukkit.getPluginManager().getPlugin("ProtocolLib") != null;

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        mapHandler = new MapHandlerFactory(this).getMapHandler();
        messageHandler = new MessageHandler(this);
        playerHandler = new PlayerHandler(this);

        new Metrics(this, 19958);

        mapHandler.loadData();

        new DoomExecutor(this);

        new PlayerHeldItemListener(this);
        new PlayerInteractEntityListener(this);
        new PlayerInteractListener(this);
        new PlayerQuitListener(this);
        new PlayerSwapHandItemsListener(this);

        new SteerVehicleListener(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        Bukkit.getOnlinePlayers().forEach(player -> {
            Pocket pocket = playerHandler.getPocket(player);
            if (pocket.getEngine() != null) pocket.stopEmulator(player);
        });
    }
}
