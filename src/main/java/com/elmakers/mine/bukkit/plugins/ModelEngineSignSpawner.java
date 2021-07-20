package com.elmakers.mine.bukkit.plugins;


import com.elmakers.mine.bukkit.plugins.commands.SpawnSign;
import org.bukkit.Bukkit;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class ModelEngineSignSpawner extends JavaPlugin implements TabExecutor, Listener {

    private static DataIO dataIO;

    public void onEnable() {
        this.getCommand("spawnsign").setExecutor(new SpawnSign());
        this.getCommand("spawnsign").setTabCompleter(new SpawnSign());

        dataIO = new DataIO(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {

    }

    public static DataIO getDataIO(){
        return dataIO;
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onServerLoadEvent(ServerLoadEvent event) {
        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                ModelEngineSignSpawner.getDataIO().loadArmorStands();

            }
        }, 80);
    }

}
