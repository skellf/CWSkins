package me.skellf.cwskins.listeners;

import me.skellf.cwskins.CWSkins;
import me.skellf.cwskins.CustomSkin;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;

public class PreventSkinUse implements Listener {

    @EventHandler
    private void preventSkinUse(PlayerInteractEvent event){
        ItemStack item = event.getItem();

        if (item != null && !item.isEmpty() && item.hasItemMeta()) {
            ItemMeta itemMeta = item.getItemMeta();
            checkSkinAndCancelEvent(itemMeta, event);
        }
    }

    @EventHandler
    private void preventEnchantSkin(PrepareItemEnchantEvent event){
        ItemStack item = event.getItem();

        if (!item.isEmpty() && item.hasItemMeta()){
            ItemMeta meta = item.getItemMeta();
            checkSkinAndCancelEvent(meta, event);
        }
    }

    private void checkSkinAndCancelEvent(ItemMeta itemMeta, Cancellable event) {
        if (itemMeta.getPersistentDataContainer().has(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING)) {
            String skinName = itemMeta.getPersistentDataContainer().get(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING);
            File skinFile = CWSkins.getSkinFile(skinName);

            if (skinFile.exists()) {
                event.setCancelled(true);
            }
        }

    }
}
