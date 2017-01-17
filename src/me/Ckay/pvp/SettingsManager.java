package me.Ckay.pvp;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import me.Ckay.pvp.SettingsManager;

public class SettingsManager {
	
	static SettingsManager instance = new SettingsManager();
    
    public static SettingsManager getInstance() {
            return instance;
    }
	
	FileConfiguration data;
    File dfile;
    
    FileConfiguration ban;
    File bfile;
    
    public void setupBanned(Plugin p) {
    	
	       
        if (!p.getDataFolder().exists()) {
                p.getDataFolder().mkdir();
        }
       
        bfile = new File(p.getDataFolder(), "banned.yml");
       
        if (!bfile.exists()) {
                try {
                        bfile.createNewFile();
                }
                catch (IOException e) {
                        Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create banned.yml!");
                }
    
    }
        
        ban = YamlConfiguration.loadConfiguration(bfile);
        
  }
    
    public FileConfiguration getBan() {
        return ban;
}
    
    public void saveBan() {
        try {
                ban.save(bfile);
        }
        catch (IOException e) {
                Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save banned.yml!");
        }
     }
    
    public void reloadBan() {
        ban = YamlConfiguration.loadConfiguration(bfile);
        
       
}

    
 //------------------
    
    
	public void setup(Plugin p) {
    	
	       
        if (!p.getDataFolder().exists()) {
                p.getDataFolder().mkdir();
        }
       
        dfile = new File(p.getDataFolder(), "data.yml");
       
        if (!dfile.exists()) {
                try {
                        dfile.createNewFile();
                }
                catch (IOException e) {
                        Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create data.yml!");
                }
    
    }
        
        data = YamlConfiguration.loadConfiguration(dfile);
        
  }
    
    public FileConfiguration getData() {
        return data;
}
    
    public void saveData() {
        try {
                data.save(dfile);
        }
        catch (IOException e) {
                Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save data.yml!");
        }
     }
    
    public void reloadData() {
        data = YamlConfiguration.loadConfiguration(dfile);
}
	
}
