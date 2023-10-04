package me.kyllian.doom.listeners.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.kyllian.doom.DoomPlugin;
import me.kyllian.doom.data.Pocket;
import mochadoom.Engine;
import mochadoom.doom.event_t;
import mochadoom.doom.evtype_t;
import mochadoom.g.Signals;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class SteerVehicleListener {

    public SteerVehicleListener(DoomPlugin doomPlugin) {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(doomPlugin, PacketType.Play.Client.STEER_VEHICLE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                Pocket pocket = doomPlugin.getPlayerHandler().getPocket(player);
                if (pocket.getEngine() == null) return;
                PacketContainer container = event.getPacket();
                float sideways = container.getFloat().read(0);
                float forward = container.getFloat().read(1);
                Engine engine = pocket.getEngine();
                if (forward > 0) {
                    // W in
                    engine.windowController.getObserver().feed(new event_t.keyevent_t(evtype_t.ev_keydown, Signals.ScanCode.SC_W));
                    // S out
                    engine.windowController.getObserver().feed(new event_t.keyevent_t(evtype_t.ev_keyup, Signals.ScanCode.SC_S));
                }
                if (forward < 0) {
                    // S in
                    engine.windowController.getObserver().feed(new event_t.keyevent_t(evtype_t.ev_keydown, Signals.ScanCode.SC_S));
                    // W out
                    engine.windowController.getObserver().feed(new event_t.keyevent_t(evtype_t.ev_keyup, Signals.ScanCode.SC_W));
                }
                if (forward == 0) {
                    // W out
                    engine.windowController.getObserver().feed(new event_t.keyevent_t(evtype_t.ev_keyup, Signals.ScanCode.SC_W));
                    // S out
                    engine.windowController.getObserver().feed(new event_t.keyevent_t(evtype_t.ev_keyup, Signals.ScanCode.SC_S));
                }

                if (sideways < 0) {
                    // D in
                    engine.windowController.getObserver().feed(new event_t.keyevent_t(evtype_t.ev_keydown, Signals.ScanCode.SC_D));
                    // A out
                    engine.windowController.getObserver().feed(new event_t.keyevent_t(evtype_t.ev_keyup, Signals.ScanCode.SC_A));
                }
                if (sideways > 0) {
                    // A in
                    engine.windowController.getObserver().feed(new event_t.keyevent_t(evtype_t.ev_keydown, Signals.ScanCode.SC_A));
                    // D out
                    engine.windowController.getObserver().feed(new event_t.keyevent_t(evtype_t.ev_keyup, Signals.ScanCode.SC_D));
                }
                if (sideways == 0) {
                    // D out
                    engine.windowController.getObserver().feed(new event_t.keyevent_t(evtype_t.ev_keyup, Signals.ScanCode.SC_D));
                    // A out
                    engine.windowController.getObserver().feed(new event_t.keyevent_t(evtype_t.ev_keyup, Signals.ScanCode.SC_A));
                }
                // Jump
                engine.windowController.getObserver().feed(new event_t.keyevent_t(container.getBooleans().read(0) ? evtype_t.ev_keydown : evtype_t.ev_keyup, Signals.ScanCode.SC_SPACE));

                // Quit
                if (container.getBooleans().read(1)) {
                    pocket.stopEmulator(player);
                    player.sendMessage(doomPlugin.getMessageHandler().getMessage("stopped"));
                    return;
                }
            }
        });

        final HashMap<UUID, Float> deltaYaw = new HashMap<>();
        final HashMap<UUID, Float> deltaPitch = new HashMap<>();


        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(doomPlugin, PacketType.Play.Client.LOOK) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                event.setCancelled(true);
                Player player = event.getPlayer();
                Pocket pocket = doomPlugin.getPlayerHandler().getPocket(player);
                if (pocket.getEngine() == null) return;
                // Send new packet instead to cancel the original one
                Engine engine = pocket.getEngine();

                float yaw = event.getPacket().getFloat().read(0);
                float pitch = event.getPacket().getFloat().read(1);

                float lastYaw = deltaYaw.getOrDefault(player.getUniqueId(), yaw);
                float lastPitch = deltaPitch.getOrDefault(player.getUniqueId(), pitch);

                deltaYaw.put(player.getUniqueId(), yaw);
                deltaPitch.put(player.getUniqueId(), pitch);

                float deltaYaw = yaw - lastYaw;
                float deltaPitch = pitch - lastPitch;

                boolean up = deltaPitch < 0;
                boolean down = deltaPitch > 0;

                boolean left = deltaYaw > 0;
                boolean right = deltaYaw < 0;

                if (up) {
                    engine.windowController.getObserver().feed(new event_t.mouseevent_t(evtype_t.ev_mouse, 0, 0, (int) deltaPitch * 10));
                } else if (down) {
                    engine.windowController.getObserver().feed(new event_t.mouseevent_t(evtype_t.ev_mouse, 0, 0, (int) deltaPitch * 10));
                }

                if (left) {
                    engine.windowController.getObserver().feed(new event_t.mouseevent_t(evtype_t.ev_mouse, 0, (int) deltaYaw * 10, 0));
                } else if (right) {
                    engine.windowController.getObserver().feed(new event_t.mouseevent_t(evtype_t.ev_mouse, 0, (int) deltaYaw * 10, 0));
                }
            }
        });
    }
}
