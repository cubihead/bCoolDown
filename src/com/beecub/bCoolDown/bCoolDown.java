package com.beecub.bCoolDown;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.beecub.bShortcut.bShortcut;

import java.util.logging.Logger;


public class bCoolDown extends JavaPlugin {
	private final bCoolDownPlayerListener playerListener = new bCoolDownPlayerListener(this);
	public static Logger log = Logger.getLogger("Minecraft");
	public static PluginDescriptionFile pdfFile;
	public static Configuration conf;
	public static Configuration confusers;
	private static bShortcut bShortcut;

	@SuppressWarnings("static-access")
	public void onEnable() {

		pdfFile = this.getDescription();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Event.Priority.Lowest, this);
		
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info("[" +  pdfFile.getName() + "]" + " version " + pdfFile.getVersion() + " is enabled!");
		
		bConfigManager bConfigManager = new bConfigManager(this);
		bConfigManager.load();
		conf = bConfigManager.conf;
		bCoolDownManager bCoolDownManager = new bCoolDownManager(this);
		bCoolDownManager.load();
		bCoolDownManager.clear();
        confusers = bCoolDownManager.confusers;
        
        setupDefaultShortcuts();
	}
	
	public void onDisable() {
	    bCoolDownManager.clear();
		log.info("[" + pdfFile.getName() + "]" + " version " + pdfFile.getVersion() + " disabled!");
	}
	
	public boolean onCommand(CommandSender sender, Command c, String commandLabel, String[] args) {
        String command = c.getName().toLowerCase();
        if (command.equalsIgnoreCase("bCoolDown")) {
            bConfigManager.reload();
            bChat.sendMessageToCommandSender(sender, "&6[" + pdfFile.getName() + "]" + " config reloaded");
            return true;
        }
        return false;
	}
	
	private void setupDefaultShortcuts() {
	    Plugin test = this.getServer().getPluginManager().getPlugin("bShortcut");
	    log.info("into");
	    if (bShortcut == null) {
            if (test != null) {
                bShortcut = (bShortcut) test;
                log.info(bShortcut.setupDefaultShortcut("/[YourCommandName]", "/[ShortCut]", "/[AlternativeShortcut]"));
            }
	    }
//	    Plugin test = this.getServer().getPluginManager().getPlugin("bShortcut");
//	    bShortcut = (bShortcut) test;
//	    log.info("into");
//        if (bShortcut == null) {
//            log.info(bShortcut.setupDefaultShortcut("/[YourCommandName]", "/[ShortCut]", "/[AlternativeShortcut]"));
//            bShortcut.setupDefaultShortcut("/[YourCommandName2]", "/[ShortCut2]", "/[AlternativeShortcut2]");
//            bShortcut.setupDefaultShortcut("/[YourCommandName3]", "/[ShortCut2]", "/[AlternativeShortcut3]");
//        }
	}
}
