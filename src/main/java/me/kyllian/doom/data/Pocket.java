package me.kyllian.doom.data;

import lombok.Getter;
import lombok.Setter;
import me.kyllian.doom.DoomPlugin;
import mochadoom.Engine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;

@Getter
public class Pocket {

    private BukkitTask arrowDespawnHandler;

    private DoomPlugin plugin;

    private Engine engine;
    public BufferedImage image;

    private ItemStack handItem = null;

    @Setter
    private Entity arrow;

    private String gameFileWithoutExtension;

    public void loadEmulator(DoomPlugin plugin, Player player) {
        this.plugin = plugin;

        createSavesFolder(plugin, player);

        image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);

        String doomPath = Paths.get(plugin.getDataFolder().getAbsolutePath(), "DOOM2.WAD").toString();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                engine = new Engine(plugin, player, "-iwad", doomPath);
                engine.run();
            } catch (Exception e) {
                Bukkit.getLogger().info("GAMEBOY: error");
                e.printStackTrace();
            }
        });

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

        engine.running = false;
        engine = null;

        arrowDespawnHandler.cancel();
        arrowDespawnHandler = null;
    }

    public void createSavesFolder(DoomPlugin plugin, Player player) {
        File savesFolder = new File(plugin.getDataFolder(), "saves/" + player.getUniqueId().toString() + "/" + gameFileWithoutExtension + ".sav");
        savesFolder.mkdirs();
    }
}
