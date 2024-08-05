package me.skellf.cwskins.listeners.restrictions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PreventEnchantSkin implements Listener {

    @EventHandler
    private void preventEnchantSkin(PrepareItemEnchantEvent event){
        ItemStack item = event.getItem();

        if (!item.getType().isAir() && item.hasItemMeta()){
            ItemMeta meta = item.getItemMeta();
            PreventSkinUse.checkSkinAndCancelEvent(meta, event);
        }
    }

}
