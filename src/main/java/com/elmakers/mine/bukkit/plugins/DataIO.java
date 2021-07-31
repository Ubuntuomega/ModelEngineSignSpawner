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
    static String root = "models.";

    public DataIO(Plugin plugin){
        this.plugin = plugin;
        this.config = plugin.getConfig();

    }

    /**
     * Saves a model in the config file
     * @param armorStand Model that will be saved.
     */
    public void saveModel(Entity armorStand){
        String uuid = armorStand.getUniqueId().toString();

        ConfigurationSection armorStandSection = config.createSection(root+uuid);

        armorStandSection.addDefault(".x",armorStand.getLocation().getX());
        armorStandSection.addDefault(".y",armorStand.getLocation().getY());
        armorStandSection.addDefault(".z",armorStand.getLocation().getZ());
        armorStandSection.addDefault(".world",armorStand.getLocation().getWorld().getName());

        config.options().copyDefaults(true);
        plugin.saveConfig();
    }

    /**
     * Removes a model saved in the config file.
     * @param uuid UUID from the model that will be removed.
     */
    public void removeModel(UUID uuid){
        if (exist(uuid)) {
            plugin.reloadConfig();
            config = plugin.getConfig();

            config.set(root+uuid, null);

            plugin.saveConfig();
        }
    }

    /**
     * Check is the uuid is saved in the config.
     * @param uuid UUID to check
     * @return true if exist, false if don't exist
     */
    public boolean exist(UUID uuid){
        if (config.contains(root+uuid.toString())){
            return true;
        }
        return false;
    }

    /**
     * Loads all models saved in the config file.
     */
    public void loadModels(){
        //String root = "armorstands";
        ConfigurationSection uuidConfigSection = config.getConfigurationSection(root);


        Set<String> armorStringList = uuidConfigSection.getKeys(false);

        plugin.getLogger().info("Searching and loading... "+armorStringList.size());

        for (String stringUuid : armorStringList){
            ConfigurationSection armorConfigSect = uuidConfigSection.getConfigurationSection(stringUuid);

            //If the config file is empty, don't load anything
            if (armorConfigSect == null){
                plugin.getLogger().info("Empty file. Model loading completed.");
                return;
            }

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
