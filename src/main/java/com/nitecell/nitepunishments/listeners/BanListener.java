package com.nitecell.nitepunishments.listeners;

import com.google.common.base.Preconditions;
import com.nitecell.nitepunishments.managers.PunishmentsManager;
import lombok.NonNull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class BanListener implements Listener {

    private final PunishmentsManager instance;

    public BanListener(@NonNull PunishmentsManager instance) {
        this.instance = Preconditions.checkNotNull(instance);
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {

        String targetUUID = event.getUniqueId().toString();
        Object statusPath = instance.getPunishments().get("punishments." + targetUUID + ".bans.status");

        if (instance.getPunishments().contains(targetUUID) && statusPath == "ACTIVE" ) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, "You have been banned from the server.");
        }

    }
}
