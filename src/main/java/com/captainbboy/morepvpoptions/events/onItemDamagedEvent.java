package com.captainbboy.morepvpoptions.events;

import com.captainbboy.morepvpoptions.MorePvpOptions;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class onItemDamagedEvent implements Listener {

    private final MorePvpOptions plugin;

    public onItemDamagedEvent(MorePvpOptions plg) {
        this.plugin = plg;
    }

    final Set<Material> SWORDS = EnumSet.of(
            Material.DIAMOND_SWORD, Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
            Material.GOLDEN_SWORD, Material.NETHERITE_SWORD, Material.LEGACY_DIAMOND_SWORD, Material.LEGACY_IRON_SWORD,
            Material.LEGACY_WOOD_SWORD, Material.LEGACY_GOLD_SWORD, Material.LEGACY_STONE_SWORD
    );

    final Set<Material> AXES = EnumSet.of(
            Material.DIAMOND_AXE, Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
            Material.GOLDEN_AXE, Material.NETHERITE_AXE, Material.LEGACY_DIAMOND_AXE, Material.LEGACY_IRON_AXE,
            Material.LEGACY_WOOD_AXE, Material.LEGACY_GOLD_AXE, Material.LEGACY_STONE_AXE
    );

    final Set<Material> OTHER_PVP_TOOLS = EnumSet.of(
            Material.TRIDENT, Material.BOW, Material.LEGACY_BOW, Material.CROSSBOW,
            Material.SHIELD, Material.LEGACY_SHIELD
    );


    final Set<Material> HELMETS = EnumSet.of(
            Material.DIAMOND_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET,
            Material.GOLDEN_HELMET, Material.NETHERITE_HELMET, Material.LEGACY_DIAMOND_HELMET, Material.LEGACY_IRON_HELMET,
            Material.LEGACY_CHAINMAIL_HELMET, Material.LEGACY_GOLD_HELMET
    );

    final Set<Material> CHESTPLATES = EnumSet.of(
            Material.DIAMOND_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE,
            Material.GOLDEN_CHESTPLATE, Material.NETHERITE_CHESTPLATE, Material.LEGACY_DIAMOND_CHESTPLATE, Material.LEGACY_IRON_CHESTPLATE,
            Material.LEGACY_CHAINMAIL_CHESTPLATE, Material.LEGACY_GOLD_CHESTPLATE
    );

    final Set<Material> LEGGINGS = EnumSet.of(
            Material.DIAMOND_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS,
            Material.GOLDEN_LEGGINGS, Material.NETHERITE_LEGGINGS, Material.LEGACY_DIAMOND_LEGGINGS, Material.LEGACY_IRON_LEGGINGS,
            Material.LEGACY_CHAINMAIL_LEGGINGS, Material.LEGACY_GOLD_LEGGINGS
    );

    final Set<Material> BOOTS = EnumSet.of(
            Material.DIAMOND_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS,
            Material.GOLDEN_BOOTS, Material.NETHERITE_BOOTS, Material.LEGACY_DIAMOND_BOOTS, Material.LEGACY_IRON_BOOTS,
            Material.LEGACY_CHAINMAIL_BOOTS, Material.LEGACY_GOLD_BOOTS
    );

    @EventHandler
    public void onItemDamaged(PlayerItemDamageEvent e) {

        if(e.isCancelled())
            return;

        if (this.plugin.getConfig().getBoolean("keepInventory.durability.enabled") == false)
            return;

        Material type = e.getItem().getType();
        if(SWORDS.contains(type) || AXES.contains(type) || OTHER_PVP_TOOLS.contains(type) || HELMETS.contains(type) || CHESTPLATES.contains(type) || LEGGINGS.contains(type) || BOOTS.contains(type)) {
            List<String> canFight = this.plugin.getPlayerDamageHandler().getCanFight();
            for (String user : canFight) {
                if(user.contains(e.getPlayer().getUniqueId().toString())) {
                    e.setCancelled(true);
                    break;
                }
            }
        }
    }

}