package me.skellf.cwskins.listeners;

import me.skellf.cwskins.CWSkins;
import me.skellf.cwskins.CustomSkin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;

public class PreventSkinUse implements Listener {

    @EventHandler
    private void preventSkinUse(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        ItemStack offHandItem = player.getInventory().getItemInOffHand();

        if (!mainHandItem.isEmpty() && mainHandItem.hasItemMeta()) {
            ItemMeta mainHandMeta = mainHandItem.getItemMeta();
            checkSkinAndCancelEvent(mainHandMeta, event);
        }

        if (!offHandItem.isEmpty() && offHandItem.hasItemMeta()) {
            ItemMeta offHandMeta = offHandItem.getItemMeta();
            checkSkinAndCancelEvent(offHandMeta, event);
        }
    }

    private void checkSkinAndCancelEvent(ItemMeta itemMeta, PlayerInteractEvent event) {
        if (itemMeta.getPersistentDataContainer().has(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING)) {
            String skinName = itemMeta.getPersistentDataContainer().get(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING);
            File skinFile = CWSkins.getSkinFile(skinName);

            if (skinFile.exists()) {
                event.setCancelled(true);
            }
        }

    }
}
