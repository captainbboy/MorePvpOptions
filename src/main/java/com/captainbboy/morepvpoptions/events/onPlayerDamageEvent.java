package com.captainbboy.morepvpoptions.events;

import com.captainbboy.morepvpoptions.MorePvpOptions;
import com.captainbboy.morepvpoptions.handlers.canFightHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class onPlayerDamageEvent implements Listener {

    private final MorePvpOptions plugin;

    public onPlayerDamageEvent(MorePvpOptions plg) {
        this.plugin = plg;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if(e.isCancelled())
            return;

        if(plugin.getConfig().getBoolean("consent.enabled") && e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player player = (Player) e.getEntity();
            Player attacker = (Player) e.getDamager();

            canFightHandler playerDamageHandler = this.plugin.getPlayerDamageHandler();
            List<String> awaitingReplies = playerDamageHandler.getAwaitingReplies();
            List<String> canFight = playerDamageHandler.getCanFight();

            ArrayList<UUID> basesPlayerIsIn = this.plugin.getplayerBaseHandler().getBasesPlayerIsIn(player.getUniqueId());
            if(basesPlayerIsIn != null && plugin.getConfig().getBoolean("consent.trespassing.enabled") && basesPlayerIsIn.contains(attacker.getUniqueId())) {
                canFight.add(player.getUniqueId()+"+"+attacker.getUniqueId());
                playerDamageHandler.setCanFight(canFight);
                attacker.sendMessage("§8§l[§4§lMorePvpOptions§8§l] §cYou and "+player.getName()+" are now engaged in PvP.");
                player.sendMessage("§8§l[§4§lMorePvpOptions§8§l] §cYou and "+attacker.getName()+" are now engaged in PvP.");
            }
            else if(canFight.contains(player.getUniqueId()+"+"+attacker.getUniqueId()) || canFight.contains(attacker.getUniqueId()+"+"+player.getUniqueId())) {}
            else if (awaitingReplies.contains(player.getUniqueId()+"+"+attacker.getUniqueId())) {
                e.setCancelled(true);

                attacker.sendMessage("§8§l[§4§lMorePvpOptions§8§l] §cYou and "+player.getName()+" are now engaged in PvP.");
                player.sendMessage("§8§l[§4§lMorePvpOptions§8§l] §cYou and "+attacker.getName()+" are now engaged in PvP.");

                awaitingReplies.remove(player.getUniqueId()+"+"+attacker.getUniqueId());
                canFight.add(player.getUniqueId()+"+"+attacker.getUniqueId());

                playerDamageHandler.setAwaitingReplies(awaitingReplies);
                playerDamageHandler.setCanFight(canFight);

                Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                    public void run() {
                        List<String> canFight = plugin.getPlayerDamageHandler().getCanFight();
                        if(canFight.contains(player.getUniqueId()+"+"+attacker.getUniqueId())) {
                            canFight.remove(player.getUniqueId()+"+"+attacker.getUniqueId());
                            playerDamageHandler.setCanFight(canFight);
                        }
                    }
                }, plugin.getConfig().getLong("consent.time.amount"));

            } else if (awaitingReplies.contains(attacker.getUniqueId()+"+"+player.getUniqueId())) {
                e.setCancelled(true);
            }
            else {
                e.setCancelled(true);
                awaitingReplies.add(attacker.getUniqueId()+"+"+player.getUniqueId());
                playerDamageHandler.setAwaitingReplies(awaitingReplies);

                attacker.sendMessage("§8§l[§4§lMorePvpOptions§8§l] §cYou have tried to attack "+player.getName()+". You and them can fight if they attack you back.");
                player.sendMessage("§8§l[§4§lMorePvpOptions§8§l] §c"+attacker.getName()+" is trying to attack you! Attack them back if you want to fight them.");

                Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                    public void run() {
                        List<String> awaitingReplies = plugin.getPlayerDamageHandler().getCanFight();
                        if(awaitingReplies.contains(player.getUniqueId()+"+"+attacker.getUniqueId())) {
                            awaitingReplies.remove(player.getUniqueId()+"+"+attacker.getUniqueId());
                            playerDamageHandler.setAwaitingReplies(awaitingReplies);
                        }
                    }
                }, 1200);
            }
        }
    }

}