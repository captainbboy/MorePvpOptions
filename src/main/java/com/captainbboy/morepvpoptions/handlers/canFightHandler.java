package com.captainbboy.morepvpoptions.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class canFightHandler {

    private List<String> awaitingReplies = new ArrayList<>();
    private List<String> canFight = new ArrayList<>();

    public void removeCanFightUsers(UUID attacker, UUID player) {
        if(canFight.contains(player+""+attacker)) {
            canFight.remove(player+"+"+attacker);
        } else if(canFight.contains(attacker+""+player)) {
            canFight.remove(attacker+"+"+player);
        }
    }

    public List<String> getAwaitingReplies(){
        return this.awaitingReplies;
    }

    public void setAwaitingReplies(List<String> newAwaitingReplies){
        this.awaitingReplies = newAwaitingReplies;
    }

    public List<String> getCanFight(){
        return this.canFight;
    }

    public void setCanFight(List<String> newCanFight){
        this.canFight = newCanFight;
    }
}
