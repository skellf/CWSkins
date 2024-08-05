package me.skellf.cwskins.listeners.restrictions;

import me.skellf.cwskins.CWSkins;
import me.skellf.cwskins.CustomSkin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;

public class DamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ItemStack item = player.getInventory().getItemInMainHand();

            if (!item.getType().isAir() && item.hasItemMeta()) {
                ItemMeta itemMeta = item.getItemMeta();

                if (itemMeta.getPersistentDataContainer().has(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING)) {
                    String skinName = itemMeta.getPersistentDataContainer().get(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING);
                    File skinFile = CWSkins.getSkinFile(skinName);

                    if (skinFile.exists()) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
