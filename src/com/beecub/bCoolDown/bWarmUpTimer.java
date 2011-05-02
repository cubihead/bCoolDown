package com.beecub.bCoolDown;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.entity.Player;

public class bWarmUpTimer extends TimerTask {
    
    bCoolDown bCoolDown;
    Player player;
    String pre;
    String message;
    
    public bWarmUpTimer(bCoolDown bCoolDown, Timer timer, Player player, String pre, String message){
        this.bCoolDown = bCoolDown;
        this.player = player;
        this.pre = pre;
        this.message = message;
    }
    public bWarmUpTimer() {
    }
    
    public void run() {            
        if(player.isOnline()) {
            bChat.sendMessageToServer("time is Up");
            bCoolDownManager.setWarmUpOK(player, pre, message);
            bWarmUpManager.removeWarmUpProcess(this.player.getName() + pre);
            player.chat(pre + message);
        }
    }
}