package me.kyllian.doom.listeners;

import me.kyllian.doom.DoomPlugin;
import me.kyllian.doom.data.Pocket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntityListener implements Listener {

    private final DoomPlugin plugin;

    public PlayerInteractEntityListener(DoomPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Pocket pocket = plugin.getPlayerHandler().getPocket(player);
        if (pocket.getEngine() != null) event.setCancelled(true);
    }
}
