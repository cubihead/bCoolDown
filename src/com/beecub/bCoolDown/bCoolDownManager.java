package com.beecub.bCoolDown;


import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;


public class bCoolDownManager {

    protected static bCoolDown bCoolDown;
    protected static Configuration confusers;
    protected File usersConfigFile;

    public bCoolDownManager(bCoolDown bCoolDown) {
        bCoolDownManager.bCoolDown = bCoolDown;
        File confFile = new File(bCoolDown.getDataFolder(), "users.yml");

        confusers = null;

        if (confFile.exists())
        {
            confusers = new Configuration(confFile);
            confusers.load();
        }
        else {
            confFile = new File(bCoolDown.getDataFolder(), "users.yml");
            confusers = new Configuration(confFile);
            confusers.save();
        }  
    }
    
    
    static void load() {
        confusers.load();
    }
    
    static void reload() {
        load();
    }
    
    static void save() {
        confusers.save();
    }
    
    static void clear() {
        List<String> players = new LinkedList<String>();
        List<String> cooldown = new LinkedList<String>();
        List<String> warmup = new LinkedList<String>();
        try{
            players.addAll(confusers.getKeys("users."));
        }
        catch(Exception e) {
            return;
        }
        int i = 0;
        while(i < players.size()) {
            // clear cooldown
            cooldown.clear();
            cooldown.addAll(confusers.getKeys("users." + players.get(i) + ".cooldown"));
            int j = 0;
            while(j < cooldown.size()) {
                confusers.removeProperty("users." + players.get(i) + ".cooldown." + cooldown.get(j));
                j++;
            }
            confusers.removeProperty("users." + players.get(i) + ".cooldown");
            // clear warmup
            warmup.clear();
            warmup.addAll(confusers.getKeys("users." + players.get(i) + ".warmup"));
            int k = 0;
            while(k < warmup.size()) {
                confusers.removeProperty("users." + players.get(i) + ".warmup." + warmup.get(k));
                k++;
            }
            confusers.removeProperty("users." + players.get(i) + ".warmup");
            
            confusers.removeProperty("users." + players.get(i));
            i++;
        }
        save();
        load();
    }
    
    static boolean coolDown(Player player, String pre, String message) {
        int coolDownSeconds = bConfigManager.getCoolDown(player, pre);
        if(coolDownSeconds > 0) {
            Date lastTime = getTime(player, pre);
            if(lastTime == null) {
                setTime(player, pre);
                return false;
            }
            else {
                Calendar calcurrTime = Calendar.getInstance();
                calcurrTime.setTime(getCurrTime());
                Calendar callastTime = Calendar.getInstance();
                callastTime.setTime(lastTime);
                long secondsBetween = secondsBetween(callastTime, calcurrTime);
                long waitSeconds = coolDownSeconds - secondsBetween;
                if(secondsBetween > coolDownSeconds) {
                    setTime(player, pre);
                    return false;
                }
                else {
                    String msg = bConfigManager.getCoolDownMessage();
                    msg = msg.replaceAll("&command&", pre);
                    msg = msg.replaceAll("&seconds&", Long.toString(waitSeconds));
                    bChat.sendMessageToPlayer(player, msg);
                    return true;
                }
            }
        }
        return false;
    }
    
    static void cancelCoolDowns(Player player) {
        List<String> cooldown = new LinkedList<String>();
        cooldown.clear();
        cooldown.addAll(confusers.getKeys("users." + player.getName() + ".cooldown"));
        int j = 0;
        while(j < cooldown.size()) {
            confusers.removeProperty("users." + player.getName() + ".cooldown." + cooldown.get(j));
            j++;
        }
        confusers.removeProperty("users." + player.getName() + ".cooldown");
    }
    
    static boolean checkCoolDownOK(Player player, String pre, String message) {
        int coolDownSeconds = bConfigManager.getCoolDown(player, pre);
        if(coolDownSeconds > 0) {
            Date lastTime = getTime(player, pre);
            if(lastTime == null) {
                return true;
            }
            else {
                Calendar calcurrTime = Calendar.getInstance();
                calcurrTime.setTime(getCurrTime());
                Calendar callastTime = Calendar.getInstance();
                callastTime.setTime(lastTime);
                long secondsBetween = secondsBetween(callastTime, calcurrTime);
                long waitSeconds = coolDownSeconds - secondsBetween;
                if(secondsBetween > coolDownSeconds) {
                    return true;
                }
                else {
                    String msg = bConfigManager.getCoolDownMessage();
                    msg = msg.replaceAll("&command&", pre);
                    msg = msg.replaceAll("&seconds&", Long.toString(waitSeconds));
                    bChat.sendMessageToPlayer(player, msg);
                    return false;
                }
            }
        }
        return true;
    }
    
    static void setTime(Player player, String pre) {
        String currTime = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        currTime = sdf.format(cal.getTime());
        confusers.setProperty("users." + player.getName() + ".cooldown." + pre, currTime);
        confusers.save();
    }
    
    static Date getCurrTime() {
        String currTime = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        currTime = sdf.format(cal.getTime());
        Date time = null;
        
        try {
            time = sdf.parse(currTime);
            return time;
         } catch(ParseException e) {
            return null;
         } 
    }
    
    static Date getTime(Player player, String pre) {
        String confTime = "";
        confTime = confusers.getString("users." + player.getName() + ".cooldown." + pre, null);
        
        if(confTime != null && confTime != "") {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            Date lastDate = null;
       
            try {
               lastDate = sdf.parse(confTime);
               return lastDate;
            } catch(ParseException e) {
               return null;
            }   
        }
        return null;              
    }
    public static long secondsBetween(Calendar startDate, Calendar endDate) {
        long secondsBetween = 0;
        
        while (startDate.before(endDate)) {
            startDate.add(Calendar.SECOND, 1);
            secondsBetween++;
        }   
        return secondsBetween;
    }
    
    static void setWarmUpOK(Player player, String pre, String message) {
        confusers.setProperty("users." + player.getName() + ".warmup." + pre, 1);
        confusers.save();
    }
    
    static boolean checkWarmUpOK(Player player, String pre, String message) {
        int ok = 0;
        ok = confusers.getInt("users." + player.getName() + ".warmup." + pre, ok);
        if(ok == 1) {
            return true;
        }
        return false;
    }
    
    static void removeWarmUpOK(Player player, String pre, String message) {
        confusers.removeProperty("users." + player.getName() + ".warmup." + pre);
        confusers.save();
    }
    
}
