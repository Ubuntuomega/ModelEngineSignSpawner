package com.elmakers.mine.bukkit.plugins;


import com.elmakers.mine.bukkit.plugins.commands.SpawnSign;
import com.elmakers.mine.bukkit.plugins.events.BreakArmorStandEvent;
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
        this.saveDefaultConfig();
        dataIO = new DataIO(this);

        this.getCommand("spawnsign").setExecutor(new SpawnSign());
        this.getCommand("spawnsign").setTabCompleter(new SpawnSign());

        getServer().getPluginManager().registerEvents(new BreakArmorStandEvent(), this);
        getServer().getPluginManager().registerEvents(this, this);

    }

    public void onDisable() {

    }

    public static DataIO getDataIO(){
        return dataIO;
    }

    //Loads the models 6 seconds after the ModelEngine loads his models. Otherwhise the models will not be loaded.
    @EventHandler (priority = EventPriority.LOWEST)
    public void onServerLoadEvent(ServerLoadEvent event) {
        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                ModelEngineSignSpawner.getDataIO().loadModels();
            }
        }, 120);
    }

}
