package me.kyllian.doom.handlers.map;

import me.kyllian.doom.DoomPlugin;
import org.bukkit.Bukkit;

public class MapHandlerFactory {

    private DoomPlugin plugin;

    public MapHandlerFactory(DoomPlugin plugin) {
        this.plugin = plugin;
    }

    public MapHandler getMapHandler() {
        String minecraftVersion = Bukkit.getVersion();
        String mainVerString = minecraftVersion.split("\\.")[1];
        mainVerString = mainVerString.replace(")", "");
        mainVerString = mainVerString.replace("(", "");
        int mainVer = Integer.parseInt(mainVerString);
        return mainVer >= 13 ? new MapHandlerNew(plugin) : new MapHandlerOld(plugin);
    }
}
