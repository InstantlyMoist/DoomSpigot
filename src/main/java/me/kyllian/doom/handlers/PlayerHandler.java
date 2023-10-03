package me.kyllian.doom.handlers;

import me.kyllian.doom.DoomPlugin;
import me.kyllian.doom.data.Pocket;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerHandler {

    private final DoomPlugin plugin;

    private final Map<Player, Pocket> pockets;

    public PlayerHandler(DoomPlugin plugin) {
        this.plugin = plugin;

        pockets =  new HashMap<>();
    }

    public void loadGame(Player player) {
        try {
            player.getInventory().setHeldItemSlot(8);
            getPocket(player).loadEmulator(plugin, player);
            plugin.getMapHandler().sendMap(player);
            if (plugin.isProtocolLib()) return;
            Location playerLocation = player.getLocation();
            playerLocation.setYaw(0);
            playerLocation.setPitch(40);
            playerLocation.setX(playerLocation.getBlockX() + 0.5);
            playerLocation.setY(playerLocation.getBlockY());
            playerLocation.setZ(playerLocation.getBlockZ() + 0.5);
            playerLocation.setPitch(0);
            playerLocation.setYaw(0);
            player.teleport(playerLocation);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public Pocket getPocket(Player player) {
        return pockets.computeIfAbsent(player, f -> new Pocket());
    }

    public void removePocket(Player player) {
        pockets.remove(player);
    }
}
