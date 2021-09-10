package com.nitecell.nitepunishments;

import com.nitecell.nitepunishments.commands.BanCommand;
import com.nitecell.nitepunishments.commands.KickCommand;
import com.nitecell.nitepunishments.managers.PunishmentsManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class NitePunishments extends JavaPlugin {

    @Getter
    private static NitePunishments instance;
    PunishmentsManager punishments = new PunishmentsManager();

    @Override
    public void onEnable() {
        instance = this;
        punishments.setup(this);
        registerCommands();
        //registerListeners();

        Bukkit.getLogger().info(ChatColor.GREEN + "[NitePunishments] has been enabled.");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(ChatColor.RED + "[NitePunishments] has been disabled.");
    }

    private void registerCommands() {
        getCommand("kick").setExecutor(new KickCommand());
        getCommand("ban").setExecutor(new BanCommand());
    }

    //private void registerListeners() {
    //Bukkit.getServer().getPluginManager().registerEvents(new punishmentsener(), this);
    //}

    public void loadConfig () {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

}