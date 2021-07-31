package com.elmakers.mine.bukkit.plugins.events;

import com.elmakers.mine.bukkit.plugins.DataIO;
import com.elmakers.mine.bukkit.plugins.ModelEngineSignSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BreakArmorStandEvent implements Listener {

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.PLAYER && event.getEntity().getType() == EntityType.ARMOR_STAND){
            Player player = (Player) event.getDamager();
            Entity armorStand = event.getEntity();
            DataIO dataIO = ModelEngineSignSpawner.getDataIO();

            if (!dataIO.exist(armorStand.getUniqueId())) {
                return;
            }

            if (player.hasPermission("ModelEngineSignSpawner.destroy")){
                dataIO.removeModel(armorStand.getUniqueId());
            } else {
                player.sendMessage("No tienes permisos tete");
                event.setCancelled(true);
            }
        }
    }

}
