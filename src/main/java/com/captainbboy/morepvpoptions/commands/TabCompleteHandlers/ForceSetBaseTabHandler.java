package com.captainbboy.morepvpoptions.commands.TabCompleteHandlers;

import com.captainbboy.morepvpoptions.MorePvpOptions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class ForceSetBaseTabHandler implements TabCompleter {

    MorePvpOptions plugin;

    public ForceSetBaseTabHandler (MorePvpOptions plg) {
        plugin = plg;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
        //create new array
        final List<String> completions = new ArrayList<>();
        List<String> options = new ArrayList<>();

        if(args.length == 1) {
            for(OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                options.add(player.getName());
            }

            StringUtil.copyPartialMatches(args[0], options, completions);

            Collections.sort(completions);
        }

        if(args.length == 2) {


            StringUtil.copyPartialMatches(args[1], options, completions);

            Collections.sort(completions);
        }

        if(args.length == 3 && args[0].equalsIgnoreCase("set")) {

            StringUtil.copyPartialMatches(args[2], options, completions);

            Collections.sort(completions);
        }

        return completions;
    }

}