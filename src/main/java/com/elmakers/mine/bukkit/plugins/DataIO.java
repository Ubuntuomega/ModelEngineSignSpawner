package com.elmakers.mine.bukkit.plugins;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.UUID;

public class DataIO implements Listener{
    Plugin plugin;
    FileConfiguration config;

    public DataIO(Plugin plugin){
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public void saveArmorStand(Entity armorStand){

        String root = "armorstands.";

        UUID uuid = armorStand.getUniqueId();
        config.addDefault(root+uuid.toString(),null);
        config.addDefault(root+uuid.toString()+".x",armorStand.getLocation().getX());
        config.addDefault(root+uuid.toString()+".y",armorStand.getLocation().getY());
        config.addDefault(root+uuid.toString()+".z",armorStand.getLocation().getZ());
        config.addDefault(root+uuid.toString()+".world",armorStand.getLocation().getWorld().getName());

        config.options().copyDefaults(true);
        plugin.saveConfig();
    }

    public void removeArmorStand(Entity armorStand){

    }

    public void loadArmorStands(){
        String root = "armorstands";
        ConfigurationSection uuidConfigSection = config.getConfigurationSection(root);

        Set<String> armorStringList = uuidConfigSection.getKeys(false);

        plugin.getLogger().info("Buscando y cargando modelos... "+armorStringList.size());

        for (String stringUuid : armorStringList){
            ConfigurationSection armorConfigSect = uuidConfigSection.getConfigurationSection(stringUuid);

            //Carga la Location del modelo guardado
            World world = Bukkit.getWorld(armorConfigSect.getString(".world"));
            plugin.getLogger().info("Buscando modelo "+stringUuid);

            UUID uuid = UUID.fromString(stringUuid);

            double x = armorConfigSect.getDouble(".x");
            double y = armorConfigSect.getDouble(".y");
            double z = armorConfigSect.getDouble(".z");
            Location location = new Location(world,x,y,z);

            //Carga el chunk donde se sitúa ese Location y recoge sus entidades

            Chunk chunk = world.getChunkAt(location);
            world.loadChunk(chunk);

            Entity[] entities = chunk.getEntities();

            //Busca y carga los modelos que hayan en ese chunk
            for (Entity entity : entities){

                if (entity.getUniqueId().equals(uuid)){
                    ModeledEntity modeledEntity = ModelEngineAPI.api.getModelManager().restoreModeledEntity(entity);
                    if (modeledEntity != null){
                        plugin.getLogger().info("Modelo cargado exitosamente :D");
                    }
                }
            }
            world.unloadChunk(chunk);
        }
        plugin.getLogger().info("¡Búsqueda terminada! :D");
    }


}
