package com.nitecell.nitepunishments.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class PunishmentsManager {

    public PunishmentsManager() { }
    static PunishmentsManager instance = new PunishmentsManager();
    public static PunishmentsManager getInstance() { return instance; }

    FileConfiguration punishments;
    File pfile;

    public void setup(Plugin p) {
        if (!(p.getDataFolder().exists())) {
            p.getDataFolder().mkdir();
            Bukkit.getServer().getLogger().fine("Data Folder did not exist, creating one!");
        }

        pfile = new File(p.getDataFolder(), "punishments.yml");
        if (!pfile.exists()) {
            try {
                pfile.createNewFile();
            }
            catch (IOException e) {
                Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create punishments.yml!");
            }
        }
        punishments = YamlConfiguration.loadConfiguration(pfile);
    }

    public FileConfiguration getPunishments() {
        return punishments;
    }
    public void savePunishments() {
        try {
            punishments.save(pfile);
        } catch (IOException e) {
            System.out.println(ChatColor.RED + "Could not save punishments.yml!");
        }
    }
    public void reloadPunishments() {
        punishments = YamlConfiguration.loadConfiguration(pfile);
    }

}