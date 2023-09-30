package me.kyllian.doom.data;

import me.kyllian.doom.DoomPlugin;
import me.kyllian.doom.helpers.ButtonToggleHelper;
import mochadoom.Engine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;

public class Pocket {

    private BukkitTask arrowDespawnHandler;

    private DoomPlugin plugin;

    private Engine engine;

    private ButtonToggleHelper buttonToggleHelper;

    private ItemStack handItem = null;
    private Entity arrow;

    private String gameFileWithoutExtension;

    public void loadEmulator(DoomPlugin plugin, Player player, String gameFile) {
        this.plugin = plugin;

        createSavesFolder(plugin, player);
        File romFile = new File(gameFile);
        this.gameFileWithoutExtension = romFile.getName();
        File saveFile = new File(plugin.getDataFolder(), "saves/" + player.getUniqueId() + "/" + gameFileWithoutExtension + ".sav");

        try {
            // TODO: Start emulator here!
//            engine = new Engine();
//            emulator = new Emulator(gameFile, saveFile, player);
//            emulator.run();
        } catch (Exception e) {
            Bukkit.getLogger().info("GAMEBOY: error");
            e.printStackTrace();
        }


//        buttonToggleHelper = new ButtonToggleHelper(plugin, emulator);

        handItem = player.getInventory().getItemInMainHand();


        arrowDespawnHandler = new BukkitRunnable() {
            @Override
            public void run() {
                if (arrow == null || arrow.isDead()) cancel();
                if (isCancelled()) return;
                arrow.setTicksLived(1);
            }
        }.runTaskTimerAsynchronously(plugin, 20, 20);
    }

    public void stopEmulator(Player player) {
        if (plugin.isProtocolLib()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    arrow.remove();
                    arrow = null;
                }
            }.runTask(plugin);
        }
        player.getInventory().setItemInMainHand(handItem);
        handItem = null;

//        emulator.stop();

        buttonToggleHelper.cancel();

        arrowDespawnHandler.cancel();
        arrowDespawnHandler = null;
    }

    public void createSavesFolder(DoomPlugin plugin, Player player) {
        File savesFolder = new File(plugin.getDataFolder(), "saves/" + player.getUniqueId().toString() + "/" + gameFileWithoutExtension + ".sav");
        savesFolder.mkdirs();
    }

//    public Emulator getEmulator() {
//        return emulator;
//    }

    public ButtonToggleHelper getButtonToggleHelper() {
        return buttonToggleHelper;
    }

    public Entity getArrow() {
        return arrow;
    }

    public void setArrow(Entity arrow) {
        this.arrow = arrow;
    }
}
