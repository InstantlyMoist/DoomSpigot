package me.kyllian.doom.commands;

import me.kyllian.doom.DoomPlugin;
import me.kyllian.doom.data.Pocket;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.file.Paths;

public class DoomExecutor implements CommandExecutor {

    private final DoomPlugin plugin;

    public DoomExecutor(DoomPlugin plugin) {
        this.plugin = plugin;

        plugin.getCommand("doom").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("player-only"));
            return true;
        }
        Player player = (Player) sender;
        Pocket pocket = plugin.getPlayerHandler().getPocket(player);
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("stop")) {
                if (pocket.getEngine() == null) {
                    player.sendMessage(plugin.getMessageHandler().getMessage("no-game"));
                    return true;
                }
                pocket.stopEmulator(player);
                player.sendMessage(plugin.getMessageHandler().getMessage("stopped"));
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                sender.sendMessage(colorTranslate("&AReloading complete!"));
                return true;
            }
            if (args[0].equalsIgnoreCase("play")) {
                if (pocket.getEngine() != null) {
                    player.sendMessage(plugin.getMessageHandler().getMessage("already-running"));
                    return true;
                }
                String wadPath = Paths.get(plugin.getDataFolder().getAbsolutePath(), plugin.getConfig().getString("wad_name")).toString();
                if (!Paths.get(wadPath).toFile().exists()) {
                    player.sendMessage(plugin.getMessageHandler().getMessage("no-wad"));
                    return true;
                }
                if (plugin.isProtocolLib()) {
                    Entity entity = player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ARROW);
                    entity.addPassenger(player);
                    entity.setSilent(true);
                    entity.setInvulnerable(true);
                    entity.setGravity(false);

                    pocket.setArrow(entity);
                }
                player.sendMessage(plugin.getMessageHandler().getMessage("now-playing"));
                plugin.getPlayerHandler().loadGame(player);
                return true;
            }
        }
        showHelp(sender);
        return true;
    }

    public void showHelp(CommandSender sender) {
        BaseComponent component = new TextComponent(plugin.getMessageHandler().getMessage("playable"));
        component.addExtra(plugin.getMessageHandler().getMessage("instructions"));
        sender.spigot().sendMessage(component);
    }

    public String colorTranslate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
