package com.nitecell.nitepunishments.commands;

import com.nitecell.nitepunishments.NitePunishments;
import com.nitecell.nitepunishments.managers.PunishmentsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class KickCommand implements CommandExecutor {

    PunishmentsManager punishments = PunishmentsManager.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("nitepunishments.kick")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Not enough arguments.");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Specified user could not be found.");
            return true;
        } else {
            String targetuuid = target.getUniqueId().toString();
            int kickid = newKickID();
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date date = new Date();

            //punishments.getPunishments().set("punishments." + targetuuid + ".username", target.getName());
            punishments.getPunishments().set("punishments." + targetuuid + ".kicks." + kickid + ".reason", getReason(args));
            punishments.getPunishments().set("punishments." + targetuuid + ".kicks." + kickid + ".time", formatter.format(date));
            punishments.getPunishments().set("punishments." + targetuuid + ".kicks." + kickid + ".location.world", target.getWorld().getName());
            punishments.getPunishments().set("punishments." + targetuuid + ".kicks." + kickid + ".location.x", target.getLocation().getX());
            punishments.getPunishments().set("punishments." + targetuuid + ".kicks." + kickid + ".location.y", target.getLocation().getY());
            punishments.getPunishments().set("punishments." + targetuuid + ".kicks." + kickid + ".location.z", target.getLocation().getZ());
            punishments.getPunishments().set("punishments." + targetuuid + ".kicks." + kickid + ".punisher", sender.getName());
            punishments.savePunishments();
            target.kickPlayer(ChatColor.RED + punishments.getPunishments().getString("punishments." + targetuuid + ".kicks." + kickid + ".reason"));
            sender.sendMessage(ChatColor.GRAY + "You have successfully kicked " + target.getName() + " from the server.");
            Bukkit.getServer().broadcastMessage(ChatColor.GREEN + target.getName() + " has been kicked by " + sender.getName());
            return true;
        }
    }

    public int newKickID() {
        if (!punishments.getPunishments().contains("stats.total-kicks")) {
            punishments.getPunishments().set("stats.total-kicks", 0);
        }
        int newid = punishments.getPunishments().getInt("stats.total-kicks") + 1;
        punishments.getPunishments().set("stats.total-kicks", newid);
        return newid;
    }

    public String getReason(String[] args) {
        if (args.length == 1) {
            return "The boot has spoken!";
        }
        StringBuilder str = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            str.append(args[i]).append(" ");
        } return str.toString();
    }
}