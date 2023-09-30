package me.kyllian.doom.listeners;

import me.kyllian.doom.DoomPlugin;
import me.kyllian.doom.data.Pocket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {

    private DoomPlugin plugin;

    public PlayerDropItemListener(DoomPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Pocket pocket = plugin.getPlayerHandler().getPocket(player);
//        if (!pocket.isEmpty())  {
//            pocket.getButtonToggleHelper().press(ButtonListener.Button.SELECT, true);
//            event.setCancelled(true);
//        }
    }
}
