package com.beecub.bCoolDown;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

public class bConfigManager {
	
	protected static bCoolDown bCoolDown;
    protected static Configuration conf;
    protected File confFile;
    static List<String> players = new LinkedList<String>();    
	
	@SuppressWarnings("static-access")
	public bConfigManager(bCoolDown bCoolDown) {
    	this.bCoolDown = bCoolDown;

    	File f = new File(bCoolDown.getDataFolder(), "config.yml");
    	conf = null;

        if (f.exists())
        {
        	conf = new Configuration(f);
        	conf.load();
        	
        }
        else {
        	this.confFile = new File(bCoolDown.getDataFolder(), "config.yml");
            this.conf = new Configuration(confFile);
            conf.setProperty("commands.cooldown./spawn", 60);
            conf.setProperty("commands.cooldown./home", 30);
            conf.setProperty("commands.warmup./give", 60);
            conf.setProperty("commands.warmup./home", 20);
            conf.setProperty("commands.options.message_cooldown", "&6Wait&e &seconds& seconds&6 before you can use command&e &command& &6again.&f");
            conf.setProperty("commands.options.message_warmup", "&6Wait&e &seconds& seconds&6 before command&e &command& &6has warmed up.&f");
            conf.setProperty("commands.options.message_warmup_alreadystarted", "&6Warm-Up process for&e &command& &6has already started.&f");
            conf.save();
        }        
    }
    
	static void load() {
	    conf.load();
    }
	
	static void reload() {
		load();
	}
	
	static int getCoolDown(Player player, String pre) {
	    int coolDown = 0;
	    coolDown = conf.getInt("commands.cooldown." + pre, coolDown);
	    return coolDown;
	}
	
	static int getWarmUp(Player player, String pre) {
	    int warmUp = 0;
	    warmUp = conf.getInt("commands.warmup." + pre, warmUp);
	    return warmUp;
	}
	
	static String getCoolDownMessage() {
	    return conf.getString("commands.options.message_cooldown", "&6Wait&e &seconds& seconds&6 before you can use command&e &command& &6again.&f");
	}
	static String getWarmUpMessage() {
        return conf.getString("commands.options.message_warmup", "&6Wait&e &seconds& seconds&6 before command&e &command& &6has warmed up.&f");
    }
	static String getWarmUpAlreadyStartedMessage() {
        return conf.getString("commands.options.message_warmup_alreadystarted", "&6Warm-Up process for&e &command& &6has already started.&f");
    }
}
