package com.captainbboy.morepvpoptions.commands;

import com.captainbboy.morepvpoptions.MorePvpOptions;
import com.captainbboy.morepvpoptions.SQLite.Database;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class SetBaseCommandHandler implements CommandExecutor {

    MorePvpOptions plugin;

    public SetBaseCommandHandler(MorePvpOptions plg) {
        this.plugin = plg;
    }

    private static final Set<Material> BEDS = EnumSet.of(
            Material.BLACK_BED, Material.BLUE_BED, Material.BROWN_BED,
            Material.CYAN_BED, Material.GRAY_BED, Material.GREEN_BED,
            Material.LIGHT_BLUE_BED, Material.LIGHT_GRAY_BED, Material.LIME_BED,
            Material.MAGENTA_BED, Material.ORANGE_BED, Material.PINK_BED,
            Material.PURPLE_BED, Material.RED_BED, Material.WHITE_BED, Material.YELLOW_BED,
            Material.LEGACY_BED
    );

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setbasearea")) {
            if(this.plugin.getConfig().getBoolean("consent.enabled") == false || this.plugin.getConfig().getBoolean("consent.trespassing.enabled") == false) {
                sender.sendMessage("§8§l[§4§lMorePvpOptions§8§l] §cYou cannot use this command because consent or trespassing is disabled.");
                return true;
            }

            if(sender instanceof Player) {
                Player playerSender = (Player) sender;
                Database db = this.plugin.getSQLiteDatabase();

                String previousTime = db.getCoolDownTime(playerSender.getUniqueId());
                Double actualTime = null;

                if(!previousTime.equalsIgnoreCase("not_set")) {
                    if(isNumeric(previousTime)) {
                        actualTime = Double.parseDouble(previousTime);
                    } else {
                       previousTime = "not_set";
                    }
                }

                if(previousTime.equalsIgnoreCase("not_set") || (actualTime != null && (new Date().getTime() > (actualTime + 86400000))) ) {

                    if(args[0] == null || !args[0].equalsIgnoreCase("confirm")) {
                        sender.sendMessage("§8§l[§4§lMorePvpOptions§8§l] §cWarning: You can only do this command once a day, and abuse of this command is punishable. If you want to continue, use the command /setbasearea confirm");
                        return true;
                    }

                    Location baseLocation = playerSender.getLocation();
                    int baseScore = 0;

                    for(Block b : getNearbyBlocks(baseLocation, 10)) {
                        if(BEDS.contains(b.getType())) {
                            baseScore = baseScore + 3;
                        } else if (b.getType() == Material.CRAFTING_TABLE || b.getType() == Material.FURNACE || b.getType() == Material.BLAST_FURNACE || b.getType() == Material.CHEST) {
                            baseScore = baseScore + 1;
                        }
                    }

                    if (baseScore > 7) {

                        sender.sendMessage("§8§l[§4§lMorePvpOptions§8§l] §aYour base has been set.");

                        this.plugin.getplayerBaseHandler().addPlayerBase(playerSender.getUniqueId(), playerSender.getLocation());
                        setCoolDown(playerSender.getUniqueId());

                    } else {
                        sender.sendMessage("§c§l(!) §cIn order to prevent the abuse of this command, you must have a decent amount of base-like blocks around you for this to work. Try adding beds, crafting tables, furnaces, or chests!");
                        return true;
                    }

                } else {
                    double difference = (actualTime + 86400000) - new Date().getTime();
                    String differenceString;

                    if(difference > 3600000) {
                        DecimalFormat f = new DecimalFormat("0.##");
                        differenceString = f.format(difference / 3600000) + " hour(s)";
                    } else if(difference > 60000) {
                        DecimalFormat f = new DecimalFormat("0.##");
                        differenceString = f.format(difference / 60000) + " minute(s)";
                    }  else {
                        DecimalFormat f = new DecimalFormat("0.##");
                        differenceString = f.format(difference / 1000) + " second(s)";
                    }

                    sender.sendMessage("§c§l(!) §cYou have already set your base today. Contact an Admin if there is a problem. You can set your base again in "+differenceString+".");
                }

            } else {
                sender.sendMessage("§c§l(!) §cOnly players can do this command!");
            }
        }
        return true;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public void setCoolDown(UUID uuid) {
        Database db = this.plugin.getSQLiteDatabase();
        String time = db.getCoolDownTime(uuid);
        if(time.equalsIgnoreCase("not_set"))
            db.addRowToCoolDown(uuid, new Date().getTime()+"");
        else
            db.setCoolDownTime(uuid, new Date().getTime()+"");
    }
}