package me.skellf.cwskins.listeners.restrictions;

import me.skellf.cwskins.CWSkins;
import me.skellf.cwskins.CustomSkin;
import me.skellf.cwskins.util.HandleSkinItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;

public class PreventAnvilUse implements Listener {

    @EventHandler
    private void preventAnvilEvent(PrepareAnvilEvent event){
        if (event.getResult() != null) {
            ItemMeta meta = event.getResult().getItemMeta();
            if (meta.getPersistentDataContainer().has(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING)){
                String skinName = meta.getPersistentDataContainer().get(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING);
                try {
                    event.setResult(HandleSkinItem.createSkinItem(CWSkins.getSkinFile(skinName)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
