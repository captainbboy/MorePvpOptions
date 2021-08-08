package com.captainbboy.morepvpoptions.SQLite;

import com.captainbboy.morepvpoptions.MorePvpOptions;

import java.util.logging.Level;

public class Error {
    public static void execute(MorePvpOptions plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(MorePvpOptions plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}