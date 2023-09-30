package me.kyllian.doom.handlers.map;

import me.kyllian.doom.DoomPlugin;

import me.kyllian.doom.data.Pocket;
import mochadoom.Engine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapHandlerNew implements MapHandler {

    private final DoomPlugin plugin;

    private Map<ItemStack, Boolean> mapsUsing;

    BufferedImage image;


    public MapHandlerNew(DoomPlugin plugin) {
        this.plugin = plugin;
        loadData();

        image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 128, 128);
        graphics.dispose();
    }

    public void loadData() {
        mapsUsing = new HashMap<>();
        File file = new File(plugin.getDataFolder(), "maps.yml");
        if (!file.exists()) plugin.saveResource("maps.yml", false);
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        List<Integer> maps = fileConfiguration.getIntegerList("maps");
        int mapAmount = plugin.getConfig().getInt("gameboys");
        int currentMapAmount = maps.size();
        if (mapAmount > currentMapAmount) {
            Bukkit.getLogger().info("Gameboy didn't find existing, predefined maps. Generating them, this may take some time...");
            World world = Bukkit.getWorlds().get(0);
            for (int i = 0; i != mapAmount - currentMapAmount; i++) {
                MapView mapView = Bukkit.createMap(world);
                maps.add((int) mapView.getId());
            }
            fileConfiguration.set("maps", maps);
            world.save();
            try {
                fileConfiguration.save(file);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        maps.forEach(mapID -> {
            ItemStack map = new ItemStack(Material.valueOf("FILLED_MAP"));
            MapMeta meta = (MapMeta) map.getItemMeta();
            meta.setMapId((int) mapID.shortValue());
            map.setItemMeta(meta);
            //map.setDurability(mapID.shortValue());
            mapsUsing.put(map, false);
        });
    }

    public void sendMap(Player player) {
        ItemStack map = mapsUsing.entrySet()
                .stream()
                .filter(mapValue -> !mapValue.getValue())
                .findFirst()
                .get()
                .getKey();

        mapsUsing.put(map, true);

        MapMeta mapMeta = (MapMeta) map.getItemMeta();

        MapView mapView = mapMeta.getMapView();

        if (mapView.getRenderers() != null) mapView.getRenderers().clear();

        MapRenderer renderer = new MapRenderer() {
            final Pocket pocket = plugin.getPlayerHandler().getPocket(player);

            @Override
            public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
                mapCanvas.drawImage(0, 0, pocket.getImage());
            }
        };

        mapView.addRenderer(renderer);

        int tickDelay = plugin.getConfig().getInt("tick_update_delay", 1);
        new BukkitRunnable() {
            final Pocket pocket = plugin.getPlayerHandler().getPocket(player);

            @Override
            public void run() {
//                if (pocket.getEmulator() == null) {
//                    mapView.removeRenderer(renderer);
//                    cancel();
//                    return;
//                }
                player.sendMap(mapView);
            }
        }.runTaskTimer(plugin, tickDelay, tickDelay);


        map.setItemMeta(mapMeta);
        player.getInventory().setItemInMainHand(map);
    }

    public void resetMap(ItemStack map) {
        mapsUsing.put(map, false);
    }
}
