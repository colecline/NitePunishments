package com.nitecell.nitepunishments.commands;

import com.nitecell.nitepunishments.managers.PunishmentsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.nitecell.nitepunishments.util.UUIDFetcher.getUUID;

public class BanCommand implements CommandExecutor {

    PunishmentsManager punishments = PunishmentsManager.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("nitepunishments.ban")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Not enough arguments.");
            return true;
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (!target.hasPlayedBefore()) {
                UUID offlineuuid = getUUID(target.getName());
                if (offlineuuid == null) {
                    sender.sendMessage(ChatColor.RED + "That user does not exist.");
                    return true;
                } else {
                    addBan(offlineuuid.toString(), sender.toString(), getReason(args).toString());
                    Bukkit.getServer().broadcastMessage(ChatColor.GREEN + target.getName() + " has been banned by " + sender.getName());
                    return true;
                }
            } else {
                addBan(target.getUniqueId().toString(), sender.toString(), getReason(args).toString());
                target.kickPlayer(ChatColor.RED + "You have been banned from the server.");
                Bukkit.getServer().broadcastMessage(ChatColor.GREEN + target.getName() + " has been banned by " + sender.getName());
                return true;
            }
        }
    }

    public void addBan(String targetUUID, String sender, String reason) {
        int banID = newBanID();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();

        punishments.getPunishments().set("punishments." + targetUUID + ".bans." + "status", "ACTIVE");
        punishments.getPunishments().set("punishments." + targetUUID + ".bans." + banID + ".reason", reason);
        punishments.getPunishments().set("punishments." + targetUUID + ".bans." + banID + ".time", formatter.format(date));
        punishments.getPunishments().set("punishments." + targetUUID + ".bans." + banID + ".punisher", sender);
        punishments.savePunishments();
    }

    public int newBanID() {
        if (!punishments.getPunishments().contains("stats.total-bans")) {
            punishments.getPunishments().set("stats.total-bans", 0);
        }
        int newid = punishments.getPunishments().getInt("stats.total-bans") + 1;
        punishments.getPunishments().set("stats.total-bans", newid);
        return newid;
    }

    public String getReason(String[] args) {
        if (args.length == 1) {
            return "The ban hammer has spoken!";
        }
        StringBuilder str = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            str.append(args[i]).append(" ");
        } return str.toString();
    }

}