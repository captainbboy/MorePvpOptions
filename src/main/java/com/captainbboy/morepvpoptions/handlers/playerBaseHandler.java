package com.captainbboy.morepvpoptions.handlers;

import com.captainbboy.morepvpoptions.MorePvpOptions;
import com.captainbboy.morepvpoptions.SQLite.Database;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;

public class playerBaseHandler {

    private final MorePvpOptions plugin;
    private boolean isInitialized = false;

    public playerBaseHandler(MorePvpOptions plg) {
        this.plugin = plg;
    }

    private HashMap<UUID, ArrayList<UUID>> basesPlayersAreIn = new HashMap<>();
    private HashMap<UUID, Location> playerBases = new HashMap<>();

    public void init() {
        if(isInitialized == true || this.plugin.getConfig().getBoolean("consent.enabled") == false || this.plugin.getConfig().getBoolean("consent.trespassing.enabled") == false)
            return;

        isInitialized = true;

        Map<String, String> stringBasesMap = this.plugin.getSQLiteDatabase().getAllPlayerBases();

        for(Map.Entry<String, String> entry : stringBasesMap.entrySet()) {
            String key = entry.getKey();
            UUID uuid = UUID.fromString(key);
            String value = entry.getValue();

            String[] valueArray = value.split("\\|\\|\\|");
            World world = Bukkit.getWorld(valueArray[0]);

            Location location = new Location(world, Double.parseDouble(valueArray[1]), Double.parseDouble(valueArray[2]), Double.parseDouble(valueArray[3]));

            playerBases.put(uuid, location);

        }
    }

    public void addPlayerBase(UUID uuid, Location location) {
        if(isInitialized == false)
            init();

        playerBases.put(uuid, location);

        String stringLocation = location.getWorld().getName() + "|||" + location.getX() + "|||" + location.getY() + "|||" + location.getZ();

        Database db = this.plugin.getSQLiteDatabase();
        if(db.getBaseLocation(uuid) == "not_set")
            db.addRowToPlayerBases(uuid, stringLocation);
        else
            db.setPlayerBase(uuid, stringLocation);

        getPlayerBase(uuid);
    }

    public Location getPlayerBase(UUID uuid) {
        if(isInitialized == false)
            init();

        String value = this.plugin.getSQLiteDatabase().getBaseLocation(uuid);
        String[] valueArray = value.split("\\|\\|\\|");
        World world = Bukkit.getWorld(valueArray[0]);

        Location location = new Location(world, Double.parseDouble(valueArray[1]), Double.parseDouble(valueArray[2]), Double.parseDouble(valueArray[3]));

        return location;
    }

    public HashMap<UUID, Location> getAllPlayerBases() {
        if(isInitialized == false)
            init();

        return this.playerBases;
    }

    public void removePlayerBase(UUID uuid, Location location) {
        if(isInitialized == false)
            init();

        playerBases.remove(uuid, location);

        this.plugin.getSQLiteDatabase().deleteRow("playerbases", uuid);
    }

    public HashMap<UUID, ArrayList<UUID>> getBasesPlayersAreIn() {
        if(isInitialized == false)
            init();

        return this.basesPlayersAreIn;
    }

    public ArrayList<UUID> getBasesPlayerIsIn(UUID uuid) {
        if(isInitialized == false)
            init();

        return this.basesPlayersAreIn.get(uuid);
    }

    public void addBasePlayerIsIn(UUID uuid, UUID baseOwner) {
        if(isInitialized == false)
            init();

        ArrayList<UUID> basesPlayerIsIn = basesPlayersAreIn.get(uuid);
        if(basesPlayerIsIn != null) {
            basesPlayerIsIn.add(baseOwner);
            basesPlayersAreIn.replace(uuid, basesPlayerIsIn);
        } else {
            ArrayList<UUID> basesPlayerIsIn2 = new ArrayList<>();
            basesPlayerIsIn2.add(baseOwner);
            basesPlayersAreIn.put(uuid, basesPlayerIsIn2);
        }
    }

    public void removeBasePlayerIsIn(UUID uuid, UUID baseOwner) {
        if(isInitialized == false)
            init();

        ArrayList<UUID> basesPlayerIsIn = basesPlayersAreIn.get(uuid);
        basesPlayerIsIn.remove(baseOwner);
        basesPlayersAreIn.replace(uuid, basesPlayerIsIn);
    }

}
