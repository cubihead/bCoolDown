package com.beecub.bCoolDown;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;


public class bCoolDownPlayerListener extends PlayerListener {
	private final bCoolDown plugin;

	public bCoolDownPlayerListener(bCoolDown instance) {
		plugin = instance;
	}
	
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }
        String message = event.getMessage();
        Player player = event.getPlayer();
        
        String pre;
        int i = message.indexOf(' ');
        if(i < 0) { i = message.length(); }
        
        pre = (String) message.subSequence(0, i);
        message = (String) message.subSequence(i, message.length());
        
        int warmUpSeconds = bConfigManager.getWarmUp(player, pre);
        if(warmUpSeconds > 0) {
            if(!bCoolDownManager.checkWarmUpOK(player, pre, message)) {
                if(bCoolDownManager.checkCoolDownOK(player, pre, message)) {
                    bWarmUpManager.startWarmUp(this.plugin, player, pre, message, warmUpSeconds);
                    event.setCancelled(true);
                }
                else {
                    event.setCancelled(true);
                }
            }
            else {
                if(bCoolDownManager.coolDown(player, pre, message)) {
                    event.setCancelled(true);                    
                }
                else {
                    bCoolDownManager.removeWarmUpOK(player, pre, message);
                }
            }
        }
        else {
            if(bCoolDownManager.coolDown(player, pre, message)) {
                event.setCancelled(true);                    
            }
        }
	}
}