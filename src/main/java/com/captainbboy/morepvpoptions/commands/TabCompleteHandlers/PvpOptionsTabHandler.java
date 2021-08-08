package com.captainbboy.morepvpoptions.commands.TabCompleteHandlers;

import com.captainbboy.morepvpoptions.MorePvpOptions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.*;

public class PvpOptionsTabHandler implements TabCompleter {

    MorePvpOptions plugin;

    public PvpOptionsTabHandler (MorePvpOptions plg) {
        plugin = plg;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
        //create new array
        final List<String> completions = new ArrayList<>();
        List<String> options = new ArrayList<>();

        if(args.length == 1) {
            options.add("set");
            options.add("get");
            StringUtil.copyPartialMatches(args[0], options, completions);

            Collections.sort(completions);
        }

        if(args.length == 2) {
            options.add("keepInventory");
            options.add("consent");
            options.add("consentTime");
            options.add("consentTrespassing");
            options.add("consentTrespassingDistance");

            if(args[0].equalsIgnoreCase("get")) {
                options.add("keepInventoryDescription");
                options.add("consentDescription");
                options.add("consentTimeDescription");
                options.add("consentTrespassingDescription");
                options.add("consentTrespassingDistanceDescription");
            }

            StringUtil.copyPartialMatches(args[1], options, completions);

            Collections.sort(completions);
        }

        if(args.length == 3 && args[0].equalsIgnoreCase("set")) {

            Map<String, String> options2 = new HashMap<>();
            options2.put("keepInventory", "keepInventory.enabled");
            options2.put("consent", "consent.enabled");
            options2.put("consentTime", "consent.time.amount");
            options2.put("consentTrespassing", "consent.trespassing.enabled");
            options2.put("consentTrespassingDistance", "consent.trespassing.distance.amount");

            for (String option : options2.keySet()) {
                if (option.equalsIgnoreCase(args[1])) {
                    if (options2.get(option).contains("enabled")) {
                        options.add("true");
                        options.add("false");
                    } else if (options2.get(option).contains("amount")) {
                        options.add("100.0");
                    }
                }
            }

            StringUtil.copyPartialMatches(args[2], options, completions);

            Collections.sort(completions);
        }

        return completions;
    }

}