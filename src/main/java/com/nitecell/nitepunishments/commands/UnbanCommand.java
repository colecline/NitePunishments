package com.nitecell.nitepunishments.commands;

import com.nitecell.nitepunishments.managers.PunishmentsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.nitecell.nitepunishments.util.UUIDFetcher.getUUID;

public class UnbanCommand implements CommandExecutor {

    PunishmentsManager punishments = PunishmentsManager.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("nitepunishments.unban")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Not enough arguments.");
            return true;
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (!target.hasPlayedBefore()) {
                UUID offlineUUID = getUUID(target.getName());
                if (offlineUUID == null) {
                    sender.sendMessage(ChatColor.RED + "That user does not exist.");
                    return true;
                } else {
                    Object statusPath = punishments.getPunishments().get("punishments." + offlineUUID + ".bans.status");

                    if (punishments.getPunishments().contains(offlineUUID.toString()) && statusPath == "ACTIVE") {
                        punishments.getPunishments().set((String) statusPath, "INACTIVE");
                        Bukkit.getServer().broadcastMessage(ChatColor.GREEN + args[0] + "has been unbanned by " + sender.getName() + ".");
                    } else {
                        sender.sendMessage(ChatColor.RED + "That user is not banned at the moment.");
                    }
                }
            } else {
                String targetUUID = target.getUniqueId().toString();
                Object statusPath = punishments.getPunishments().get("punishments." + target.getUniqueId().toString() + ".bans.status");

                if (punishments.getPunishments().contains(targetUUID.toString()) && statusPath == "ACTIVE") {
                    punishments.getPunishments().set((String) statusPath, "INACTIVE");
                    Bukkit.getServer().broadcastMessage(ChatColor.GREEN + args[0] + "has been unbanned by " + sender.getName() + ".");
                } else {
                    sender.sendMessage(ChatColor.RED + "That user is not banned at the moment.");
                }
            }
        }
        return false;
    }
}
