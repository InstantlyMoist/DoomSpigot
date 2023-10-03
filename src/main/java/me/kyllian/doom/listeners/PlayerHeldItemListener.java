package me.kyllian.doom.listeners;

import me.kyllian.doom.DoomPlugin;
import me.kyllian.doom.data.Pocket;
import mochadoom.doom.event_t;
import mochadoom.doom.evtype_t;
import mochadoom.g.Signals;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class PlayerHeldItemListener implements Listener {

    private final DoomPlugin plugin;
    private final Signals.ScanCode[] scanCodes = new Signals.ScanCode[] {
            Signals.ScanCode.SC_1,
            Signals.ScanCode.SC_2,
            Signals.ScanCode.SC_3,
            Signals.ScanCode.SC_4,
            Signals.ScanCode.SC_5,
            Signals.ScanCode.SC_6,
            Signals.ScanCode.SC_7,
            Signals.ScanCode.SC_8,
            Signals.ScanCode.SC_9,
    };

    public PlayerHeldItemListener(DoomPlugin plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerHeldItem(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Pocket pocket = plugin.getPlayerHandler().getPocket(player);
        if (pocket.getEngine() == null) return; // No game running
        event.setCancelled(true);
        int newSlot = event.getNewSlot();
        if (newSlot == 8) return;
        pocket.getEngine().windowController.getObserver().feed(new event_t.keyevent_t(evtype_t.ev_keydown, scanCodes[newSlot]));
        Bukkit.getScheduler().runTaskLater(plugin, () -> pocket.getEngine().windowController.getObserver().feed(new event_t.keyevent_t(evtype_t.ev_keyup, scanCodes[newSlot])), 5L);
    }
}
