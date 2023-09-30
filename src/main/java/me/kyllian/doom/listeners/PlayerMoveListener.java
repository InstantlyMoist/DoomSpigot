package me.kyllian.doom.listeners;

import me.kyllian.doom.DoomPlugin;
import me.kyllian.doom.data.Pocket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private DoomPlugin plugin;

    public PlayerMoveListener(DoomPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Pocket pocket = plugin.getPlayerHandler().getPocket(player);
//        if (pocket.isEmpty()) return;
//        double diffX = event.getTo().getX() - event.getFrom().getX();
//        double diffZ = event.getTo().getZ() - event.getFrom().getZ();
//        pocket.getButtonToggleHelper().press(ButtonListener.Button.LEFT, diffX > 0.01);
//        pocket.getButtonToggleHelper().press(ButtonListener.Button.RIGHT, diffX < -0.01);
//        pocket.getButtonToggleHelper().press(ButtonListener.Button.UP, diffZ > 0.01);
//        pocket.getButtonToggleHelper().press(ButtonListener.Button.DOWN, diffZ < -0.01);
//        event.setTo(event.getFrom());
    }

}
