package com.elmakers.mine.bukkit.plugins.commands;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HideSign implements CommandExecutor {
    private static final ChatColor ERROR_PREFIX = ChatColor.RED;


    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sendError(sender, "May only be used in-game");
            return true;
        }

        Player player = (Player) sender;

        Entity entity = player.getNearbyEntities(4, 4, 4).get(0);
        UUID uuid = entity.getUniqueId();

        player.sendMessage(uuid.toString());
        ModeledEntity modeledEntity = ModelEngineAPI.api.getModelManager().getModeledEntity(uuid);



        for (ActiveModel activeModel : modeledEntity.getAllActiveModel().values()) {
            activeModel.hideModel(player);
        }

        return true;
}


    protected void sendError(CommandSender sender, String string) {
        sender.sendMessage(ERROR_PREFIX + string);
    }
}
