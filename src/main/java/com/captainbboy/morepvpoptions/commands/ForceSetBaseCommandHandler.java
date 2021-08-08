package com.captainbboy.morepvpoptions.commands;

import com.captainbboy.morepvpoptions.MorePvpOptions;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.Set;

public class ForceSetBaseCommandHandler implements CommandExecutor {

    MorePvpOptions plugin;

    public ForceSetBaseCommandHandler(MorePvpOptions plg) {
        this.plugin = plg;
    }


    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("forcesetbasearea"))
            return true;

        if (!sender.hasPermission("morepvpoptions.forcesetbasearea") && !sender.isOp()) {
            sender.sendMessage("§c§l(!) §cNo permission!");
            return true;
        }

        if(args.length < 1 || (sender instanceof ConsoleCommandSender && args.length != 5) || args.length > 5) {
            sender.sendMessage("§c§l(!) §cInvalid usage of that command! Correct usage: "+command.getUsage());
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if(target == null) {
            sender.sendMessage("§c§l(!) §cInvalid player name!");
            return true;
        }

        if(args.length == 1) {
            if(!(sender instanceof Player)) {
                sender.sendMessage("§c§l(!) §cInvalid usage of that command! Correct usage: "+command.getUsage().replaceAll("\\[", "<").replaceAll("\\]", ">"));
                return true;
            }
            Player senderP = (Player) sender;
            this.plugin.getplayerBaseHandler().addPlayerBase(target.getUniqueId(), senderP.getLocation());
            sender.sendMessage("§8§l[§4§lMorePvpOptions§8§l] §aYou set "+target.getName()+"'s base to your current location.");
            return true;
        }

        if(args.length > 1 && args.length < 5) {
            sender.sendMessage("§c§l(!) §cInvalid usage of that command! Correct usage: "+command.getUsage());
            return true;
        }

        if(args.length == 5) {
            World world = Bukkit.getWorld(args[1]);
            if(world == null) {
                sender.sendMessage("§c§l(!) §cInvalid world name!");
                return true;
            }
            if(!isNumeric(args[2])) {
                sender.sendMessage("§c§l(!) §cInvalid X coordinate (not a number)!");
                return true;
            }
            if(!isNumeric(args[3])) {
                sender.sendMessage("§c§l(!) §cInvalid X coordinate (not a number)!");
                return true;
            }
            if(!isNumeric(args[4])) {
                sender.sendMessage("§c§l(!) §cInvalid X coordinate (not a number)!");
                return true;
            }
            Double x = Double.parseDouble(args[2]);
            Double y = Double.parseDouble(args[2]);
            Double z = Double.parseDouble(args[2]);
            Location loc = new Location(world, x, y, z);

            sender.sendMessage("§8§l[§4§lMorePvpOptions§8§l] §aYou set "+target.getName()+"'s base to "+x+" "+y+" "+z+".");
            this.plugin.getplayerBaseHandler().addPlayerBase(target.getUniqueId(), loc);
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
}