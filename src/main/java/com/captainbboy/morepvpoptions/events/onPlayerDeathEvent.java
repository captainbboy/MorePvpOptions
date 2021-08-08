package com.captainbboy.morepvpoptions.events;

import com.captainbboy.morepvpoptions.MorePvpOptions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class onPlayerDeathEvent implements Listener {

    private final MorePvpOptions plugin;

    public onPlayerDeathEvent(MorePvpOptions plg) {
        this.plugin = plg;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if(e.isCancelled())
            return;

        Player player = e.getEntity();
        Player killer = player.getKiller();

        if(killer != null && !killer.equals(player)) {
            this.plugin.getPlayerDamageHandler().removeCanFightUsers(player.getUniqueId(), killer.getUniqueId());

            if (this.plugin.getConfig().getBoolean("keepInventory.enabled")) {
                e.setKeepInventory(true);
                e.getDrops().clear();
            }

        }
    }

}
