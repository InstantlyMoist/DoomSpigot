package me.kyllian.doom.listeners;

import me.kyllian.doom.DoomPlugin;
import me.kyllian.doom.data.Pocket;
import mochadoom.Engine;
import mochadoom.doom.event_t;
import mochadoom.doom.evtype_t;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.function.Consumer;

public class PlayerInteractListener implements Listener {

    private final DoomPlugin plugin;

    public PlayerInteractListener(DoomPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Pocket pocket = plugin.getPlayerHandler().getPocket(player);
        Bukkit.broadcastMessage("working...");
        if (pocket.getEngine() == null) return;
        Bukkit.broadcastMessage("Not null");
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Engine engine = pocket.getEngine();
            Bukkit.broadcastMessage("Right click");
            engine.windowController.getObserver().feed(new event_t.mouseevent_t(evtype_t.ev_mouse, event_t.MOUSE_LEFT, 0, 0));
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                engine.windowController.getObserver().feed(new event_t.mouseevent_t(evtype_t.ev_mouse, 0, 0, 0));
            }, 5L);
//           engine.windowController.getObserver().observe();
//            pocket.getButtonToggleHelper().press(ButtonListener.Button.B, true);
        }
        event.setCancelled(true);
    }
}
