package com.elmakers.mine.bukkit.plugins.commands;

import com.elmakers.mine.bukkit.plugins.ModelEngineSignSpawner;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class CheckSign implements CommandExecutor {

    private static final ChatColor ERROR_PREFIX = ChatColor.RED;


    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        sender.sendMessage("Buscando y cargando modelos...");
        ModelEngineSignSpawner.getDataIO().loadArmorStands();
        sender.sendMessage("¡Tarea completada!");
        return true;
    }


    protected void sendError(CommandSender sender, String string) {
        sender.sendMessage(ERROR_PREFIX + string);
    }
}