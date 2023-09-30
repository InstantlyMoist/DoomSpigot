package me.kyllian.doom.listeners;

import me.kyllian.doom.DoomPlugin;
import me.kyllian.doom.data.Pocket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private DoomPlugin plugin;

    public PlayerInteractListener(DoomPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Pocket pocket = plugin.getPlayerHandler().getPocket(player);
//        if (pocket.isEmpty()) return;
//        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
//            pocket.getButtonToggleHelper().press(ButtonListener.Button.B, true);

//        event.setCancelled(true);
    }
}
