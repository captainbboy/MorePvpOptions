package com.captainbboy.morepvpoptions.commands;

import com.captainbboy.morepvpoptions.MorePvpOptions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class PvpOptionsCommandHandler implements CommandExecutor {

    MorePvpOptions plugin;

    public PvpOptionsCommandHandler(MorePvpOptions plg) {
        this.plugin = plg;
    }

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("pvpoptions")) {
            if (!sender.hasPermission("morepvpoptions.pvpoptions") && !sender.isOp()) {
                sender.sendMessage("§c§l(!) §cNo permission!");
                return true;
            }

            if(args.length < 2) {
                sender.sendMessage("§c§l(!) §cInvalid usage of that command! Correct usage: "+command.getUsage());
                return true;
            }
            Map<String, String> options = new HashMap<>();
            options.put("keepInventory", "keepInventory.enabled");
            options.put("consent", "consent.enabled");
            options.put("consentTime", "consent.time.amount");
            options.put("consentTrespassing", "consent.trespassing.enabled");
            options.put("consentTrespassingDistance", "consent.trespassing.distance.amount");

            if(args[0].equalsIgnoreCase("get")){
                options.put("keepInventoryDescription", "keepInventory.description");
                options.put("consentDescription", "consent.description");
                options.put("consentTimeDescription", "consent.time.description");
                options.put("consentTrespassingDescription", "consent.trespassing.description");
                options.put("consentTrespassingDistanceDescription", "consent.trespassing.distance.description");
                for (String option : options.keySet()) {
                    if (option.equalsIgnoreCase(args[1])) {
                        sender.sendMessage("§8§l[§4§lMorePvpOptions§8§l] §a"+option+"'s value is: "+plugin.getConfig().getString(options.get(option)));
                        return true;
                    }
                }
                sender.sendMessage("§c§l(!) §cInvalid option! List of options: "+String.join(", ", options.keySet().toArray(new String[options.size()])));
            } else if(args[0].equalsIgnoreCase("set")){
                for (String option : options.keySet()) {
                    if(option.equalsIgnoreCase(args[1])) {
                        if(options.get(option).contains("enabled")) {
                            if(args[2].equalsIgnoreCase("true")) {
                                plugin.getConfig().set(options.get(option), true);
                                plugin.saveConfig();
                                sender.sendMessage("§8§l[§4§lMorePvpOptions§8§l] §a"+option+" has been set to true.");
                            } else if (args[2].equalsIgnoreCase("false")) {
                                plugin.getConfig().set(options.get(option), false);
                                plugin.saveConfig();
                                sender.sendMessage("§8§l[§4§lMorePvpOptions§8§l] §a"+option+" has been set to false.");
                            } else {
                                sender.sendMessage("§c§l(!) §cInvalid value! Accepted values: true, false");
                            }
                        }  else if (options.get(option).contains("amount")) {
                            if(isNumeric(args[2])) {
                                plugin.getConfig().set(options.get(option), Double.parseDouble(args[2]));
                                plugin.saveConfig();
                                sender.sendMessage("§8§l[§4§lMorePvpOptions§8§l] §a"+option+" has been set to "+args[2]+".");
                            } else {
                                sender.sendMessage("§c§l(!) §cInvalid value! Accepted values: number");
                            }
                        }
                        return true;
                    }
                }
                sender.sendMessage("§c§l(!) §cInvalid option! List of options: "+String.join(", ", options.keySet().toArray(new String[options.size()])));
            } else {
                sender.sendMessage("§c§l(!) §cInvalid usage of that command! Correct usage: "+command.getUsage());
                return true;
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
}
