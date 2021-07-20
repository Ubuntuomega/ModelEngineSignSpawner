package com.elmakers.mine.bukkit.plugins;

import java.util.Set;
import java.util.UUID;

import com.elmakers.mine.bukkit.plugins.commands.CheckSign;
import com.elmakers.mine.bukkit.plugins.commands.ShowSign;
import com.elmakers.mine.bukkit.plugins.commands.HideSign;
import com.elmakers.mine.bukkit.plugins.commands.SpawnSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableSet;

public class ModelEngineSignSpawner extends JavaPlugin implements TabExecutor, Listener {
    private static final ChatColor CHAT_PREFIX = ChatColor.AQUA;
    private static final ChatColor ERROR_PREFIX = ChatColor.RED;
    private static final int MAX_RANGE = 64;
    private static final Set<Material> transparent = ImmutableSet.of(Material.AIR, Material.CAVE_AIR, Material.TALL_GRASS);

    private static DataIO dataIO;

    public void onEnable() {
        this.getCommand("spawnsign").setExecutor(new SpawnSign());
        this.getCommand("spawnsign").setTabCompleter(new SpawnSign());

        this.getCommand("showsign").setExecutor(new ShowSign());
        this.getCommand("hidesign").setExecutor(new HideSign());
        this.getCommand("checksign").setExecutor(new CheckSign());

        Entity entity = Bukkit.getEntity(UUID.fromString("8409f3cd-ae3c-4440-b87b-0339912c5a26"));



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
