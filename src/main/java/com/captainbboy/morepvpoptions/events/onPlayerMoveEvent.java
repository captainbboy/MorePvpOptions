package com.captainbboy.morepvpoptions.events;

import com.captainbboy.morepvpoptions.MorePvpOptions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class onPlayerMoveEvent implements Listener {

    private final MorePvpOptions plugin;

    public onPlayerMoveEvent(MorePvpOptions plg) {
        this.plugin = plg;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if(e.isCancelled())
            return;

        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
            return;
        }

        if(this.plugin.getConfig().getBoolean("consent.enabled") == false || this.plugin.getConfig().getBoolean("consent.trespassing.enabled") == false)
            return;

        Player player = e.getPlayer();
        Location newLocation = e.getTo();

        HashMap<UUID, Location> playerBases = this.plugin.getplayerBaseHandler().getAllPlayerBases();
        ArrayList<UUID> basesPlayerIsIn = this.plugin.getplayerBaseHandler().getBasesPlayerIsIn(player.getUniqueId());
        Double distanceLogo = this.plugin.getConfig().getDouble("consent.trespassing.distance.amount");

        for (UUID uuid : playerBases.keySet()) {
            if (!uuid.equals(player.getUniqueId())) {
                Location enemyBase = playerBases.get(uuid);
                if (newLocation.distance(enemyBase) < distanceLogo) {
                    if (basesPlayerIsIn == null || !basesPlayerIsIn.contains(uuid)) {
                        player.sendMessage("§8§l[§4§lMorePvpOptions§8§l] §cYou have entered " + Bukkit.getOfflinePlayer(uuid).getName() + "'s base area. They can attack you without consent here. Be warned.");
                        this.plugin.getplayerBaseHandler().addBasePlayerIsIn(player.getUniqueId(), uuid);
                    }
                } else {
                    if (basesPlayerIsIn != null && basesPlayerIsIn.contains(uuid)) {
                        this.plugin.getplayerBaseHandler().removeBasePlayerIsIn(player.getUniqueId(), uuid);
                        player.sendMessage("§8§l[§4§lMorePvpOptions§8§l] §aYou have left " + Bukkit.getOfflinePlayer(uuid).getName() + "'s base area. They cannot attack you here.");
                    }
                }
            }
        }
    }
}
