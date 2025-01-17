package com.elmakers.mine.bukkit.plugins.commands;

import com.elmakers.mine.bukkit.plugins.ModelEngineSignSpawner;
import com.google.common.collect.ImmutableSet;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class SpawnSign implements TabExecutor, CommandExecutor {

    private static final ChatColor CHAT_PREFIX = ChatColor.AQUA;
    private static final ChatColor ERROR_PREFIX = ChatColor.RED;
    private static final int MAX_RANGE = 64;
    private static final Set<Material> transparent = ImmutableSet.of(Material.AIR, Material.CAVE_AIR, Material.TALL_GRASS);

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (args.length == 0) {

            return false;
        }
        if (!(sender instanceof Player)) {
            sendError(sender, "May only be used in-game");
            return true;
        }
        String modelName = args[0];
        EntityType entityType = EntityType.ARMOR_STAND;

        Player player = (Player)sender;
        Block block = player.getTargetBlock(transparent, MAX_RANGE);
        if (transparent.contains(block.getType())) {
            sendError(sender, "You must be looking at a block");
            return true;
        }

        block = block.getRelative(BlockFace.UP);
        Location posicion = block.getLocation();

        //Center the armorstand in the block
        posicion.setX(posicion.getX()+0.5);
        posicion.setZ(posicion.getZ()+0.5);

        int yaw = (int) player.getEyeLocation().getYaw();

        if (yaw >=-135 && yaw <-45) { //East
            posicion.setYaw(90);
        }
        else if(yaw >= -45 && yaw < 45) { //South
            posicion.setYaw(180);
        }
        else if(yaw >= 45 && yaw < 135) { //West
            posicion.setYaw(-90);
        }
        else  { //North
            posicion.setYaw(0);
        }

        Entity mob =  block.getWorld().spawnEntity(posicion, entityType);

        mob.setGravity(false);
        mob.setInvulnerable(true);

        if (!mob.isValid()) {
            sendError(sender, "Failed to spawn mob of type: " + ChatColor.WHITE + entityType);
            return true;
        }

        ActiveModel model = ModelEngineAPI.api.getModelManager().createActiveModel(modelName);
        if (model == null) {
            sendError(sender, "Failed to load model: " + ChatColor.WHITE + modelName);
            return true;
        }

        ModeledEntity modeledEntity = ModelEngineAPI.api.getModelManager().createModeledEntity(mob);
        if (modeledEntity == null) {
            sendError(sender, "Failed to create modelled entity");
            return false;
        }

        modeledEntity.addActiveModel(model);
        modeledEntity.detectPlayers();

        modeledEntity.setInvisible(true);


        ModelEngineSignSpawner.getDataIO().saveModel(mob);


        return true;
    }

    protected void sendMessage(CommandSender sender, String string) {
        sender.sendMessage(CHAT_PREFIX + string);
    }

    protected void sendError(CommandSender sender, String string) {
        sender.sendMessage(ERROR_PREFIX + string);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String completeCommand = args.length > 0 ? args[args.length - 1] : "";

        completeCommand = completeCommand.toLowerCase();
        Collection<String> allOptions = onTabComplete(command.getName(), args);
        List<String> options = new ArrayList<>();
        for (String option : allOptions) {
            String lowercase = option.toLowerCase();
            if (lowercase.startsWith(completeCommand)) {
                options.add(option);
            }
        }
        Collections.sort(options);

        return options;
    }

    public List<String> onTabComplete(String command, String[] args) {
        List<String> options = new ArrayList<>();
        if (command.equals("spawnsign")) {
            if (args.length == 1) {
                Collection<String> models = ModelEngineAPI.api.getModelManager().getModelRegistry().getRegisteredModel().keySet();
                options.addAll(models);
            } else if (args.length == 2) {
                for (EntityType entityType : EntityType.values()) {
                    options.add(entityType.name().toLowerCase());
                }
            }
        }
        return options;
    }

}
