package me.kyllian.doom.tasks;

import org.bukkit.scheduler.BukkitRunnable;

public class MovementStopper extends BukkitRunnable {

    private long lastUpdate;
//    private Emulator emulator;

//    public MovementStopper(DoomPlugin plugin, Emulator emulator) {
//        this.emulator = emulator;
//        this.lastUpdate = System.currentTimeMillis();
//        runTaskTimer(plugin, 5, 5);
//    }
//
    public void run() {
//        if (emulator == null) cancel();
//        if (System.currentTimeMillis() - lastUpdate > 100 && emulator != null) {
//            emulator.buttonUp = false;
//            emulator.buttonDown = false;
//            emulator.buttonLeft = false;
//            emulator.buttonRight = false;
//        }
    }

    public void update() {
        lastUpdate = System.currentTimeMillis();
    }
}
