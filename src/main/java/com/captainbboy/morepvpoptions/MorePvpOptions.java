package com.captainbboy.morepvpoptions;

import com.captainbboy.morepvpoptions.SQLite.Database;
import com.captainbboy.morepvpoptions.SQLite.SQLite;
import com.captainbboy.morepvpoptions.commands.ForceSetBaseCommandHandler;
import com.captainbboy.morepvpoptions.commands.PvpOptionsCommandHandler;
import com.captainbboy.morepvpoptions.commands.SetBaseCommandHandler;
import com.captainbboy.morepvpoptions.commands.TabCompleteHandlers.ForceSetBaseTabHandler;
import com.captainbboy.morepvpoptions.commands.TabCompleteHandlers.PvpOptionsTabHandler;
import com.captainbboy.morepvpoptions.events.onPlayerDamageEvent;
import com.captainbboy.morepvpoptions.events.onPlayerDeathEvent;
import com.captainbboy.morepvpoptions.events.onPlayerMoveEvent;
import com.captainbboy.morepvpoptions.handlers.canFightHandler;
import com.captainbboy.morepvpoptions.handlers.playerBaseHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class MorePvpOptions extends JavaPlugin {

    public FileConfiguration config = this.getConfig();
    private canFightHandler PlayerDamageHandler;
    private playerBaseHandler PlayerBaseHandler;
    private Database db;
    // Color Character: §


    @Override
    public void onEnable() {

        // If no config.yml, create config.yml
        saveDefaultConfig();

        // Config Handler:
        config.addDefault("keepInventory.enabled", true);
        config.set("keepInventory.description", "This option will enable keepInventory on Pvp Death. (Only when a player is killed by another player.)");
        config.addDefault("keepInventory.durability.enabled", true);
        config.set("keepInventory.durability.description", "This option will stop armor and weapons from taking durability damage when in a PvP fight.)");

        config.addDefault("consent.enabled", true);
        config.set("consent.description", "Requires each player to hit each other before they are allowed to do damage to each other.");
        config.addDefault("consent.time.amount", 3600.0);
        config.set("consent.time.description", "Amount of ticks (20 in a second) that a fight lasts.");
        config.addDefault("consent.trespassing.enabled", true);
        config.set("consent.trespassing.description", "If enabled, a player can set their base, and if another player is within a _ block radius of that block, the owner can fight the trespasser without waiting for consent.");
        config.addDefault("consent.trespassing.distance.amount", 75.0);
        config.set("consent.trespassing.distance.description", "The radius that a player's base extends to (in terms of trespassing).");

        config.options().copyDefaults(true);
        saveConfig();

        // SQLite Database Handler:
        this.db = new SQLite(this);
        this.db.load();
        this.db.initialize();

        // Event Handlers:
        getServer().getPluginManager().registerEvents(new onPlayerDamageEvent(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerDeathEvent(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerMoveEvent(this), this);

        // Command Handlers:
        getCommand("pvpoptions").setExecutor(new PvpOptionsCommandHandler(this));
        getCommand("pvpoptions").setTabCompleter(new PvpOptionsTabHandler(this));

        getCommand("setbasearea").setExecutor(new SetBaseCommandHandler(this));

        getCommand("forcesetbasearea").setExecutor(new ForceSetBaseCommandHandler(this));
        getCommand("forcesetbasearea").setTabCompleter(new ForceSetBaseTabHandler(this));

        // Define other handlers
        PlayerDamageHandler = new canFightHandler();
        PlayerBaseHandler = new playerBaseHandler(this);
        PlayerBaseHandler.init();

        getServer().getConsoleSender().sendMessage("§8§l[§4§lMorePvpOptions§8§l] §c§lPlugin loaded.");

    }

    public canFightHandler getPlayerDamageHandler() {
        return this.PlayerDamageHandler;
    }

    public playerBaseHandler getplayerBaseHandler() {
        return this.PlayerBaseHandler;
    }

    public Database getSQLiteDatabase() {
        return this.db;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
