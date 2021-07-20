package com.elmakers.mine.bukkit.plugins;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.UUID;

//Patch for register the models in ModelEngine, otherwhise only appear the armorstand instead of the model.
public class DataIO implements Listener{
    Plugin plugin;
    FileConfiguration config;

    static String root = "armorstands.";
    public DataIO(Plugin plugin){
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public void saveArmorStand(Entity armorStand){
        UUID uuid = armorStand.getUniqueId();

        config.addDefault(root+uuid.toString(),null);
        config.addDefault(root+uuid.toString()+".x",armorStand.getLocation().getX());
        config.addDefault(root+uuid.toString()+".y",armorStand.getLocation().getY());
        config.addDefault(root+uuid.toString()+".z",armorStand.getLocation().getZ());
        config.addDefault(root+uuid.toString()+".world",armorStand.getLocation().getWorld().getName());

        config.options().copyDefaults(true);
        plugin.saveConfig();
    }

    // TODO - Remove the armorstand when it breaks
    public void removeArmorStand(Entity armorStand){

    }

    public void loadArmorStands(){
        String root = "armorstands";
        ConfigurationSection uuidConfigSection = config.getConfigurationSection(root);

        Set<String> armorStringList = uuidConfigSection.getKeys(false);

        plugin.getLogger().info("Searching and loading... "+armorStringList.size());

        for (String stringUuid : armorStringList){
            ConfigurationSection armorConfigSect = uuidConfigSection.getConfigurationSection(stringUuid);

            //Load the Location of the saved model
            World world = Bukkit.getWorld(armorConfigSect.getString(".world"));
            plugin.getLogger().info("Searching armorstand "+stringUuid+" ...");

            UUID uuid = UUID.fromString(stringUuid);

            double x = armorConfigSect.getDouble(".x");
            double y = armorConfigSect.getDouble(".y");
            double z = armorConfigSect.getDouble(".z");
            Location location = new Location(world,x,y,z);

            //Load and gets the entities of the chunk where is the model
            Chunk chunk = world.getChunkAt(location);
            world.loadChunk(chunk);

            Entity[] entities = chunk.getEntities();

            //Search and register the model by the armorstand uuid
            for (Entity entity : entities){

                if (entity.getUniqueId().equals(uuid)){
                    ModeledEntity modeledEntity = ModelEngineAPI.api.getModelManager().restoreModeledEntity(entity);
                    if (modeledEntity != null){
                        plugin.getLogger().info("Model loaded sucesfull!");
                    }
                }
            }

            world.unloadChunk(chunk);
        }
        plugin.getLogger().info("Model loading completed.");
    }


}
