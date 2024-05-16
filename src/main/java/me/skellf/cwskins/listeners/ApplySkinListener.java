package me.skellf.cwskins.listeners;

import me.skellf.cwskins.CWSkins;
import me.skellf.cwskins.CustomSkin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.*;

public class ApplySkinListener implements Listener {

    private final Map<UUID, Long> lastSkinApplyTime = new HashMap<>();
    private final Map<UUID, Long> lastSkinClearTime = new HashMap<>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack cursorItem = event.getCursor();
        ItemStack clickedItem = event.getCurrentItem();
        if (cursorItem != null && clickedItem != null && event.getClickedInventory() != null && cursorItem.getType() != Material.AIR && clickedItem.getType() != Material.AIR) {
            ItemMeta cursorMeta = cursorItem.getItemMeta();
            if (cursorMeta != null && cursorMeta.getPersistentDataContainer().has(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING)) {
                String skinName = cursorMeta.getPersistentDataContainer().get(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING);
                CustomSkin skin;
                try {
                    skin = CustomSkin.fromFile(CWSkins.getSkinFile(skinName));
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                if (skin != null && skinName != null) {
                    if (clickedItem.getType() == skin.getMaterial() && !clickedItem.getItemMeta().hasCustomModelData() && !clickedItem.getItemMeta().getPersistentDataContainer().has(CustomSkin.APPLIED_SKIN_KEY, PersistentDataType.STRING)) {
                        long lastApplyTime = lastSkinApplyTime.getOrDefault(player.getUniqueId(), 0L);
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - lastApplyTime >= 5000) {
                            lastSkinApplyTime.put(player.getUniqueId(), currentTime);
                            ItemMeta clickedItemMeta = clickedItem.getItemMeta();
                            clickedItemMeta.setCustomModelData(skin.getCustomModelData());
                            clickedItemMeta.getPersistentDataContainer().set(CustomSkin.APPLIED_SKIN_KEY, PersistentDataType.STRING, skinName);
                            clickedItem.setItemMeta(clickedItemMeta);

                            event.setCursor(null);
                            event.setCancelled(true);
                        } else {
                            player.sendMessage(MiniMessage.miniMessage().deserialize(" <gray>|</gray> <red>Подождите <bold>5</bold> секунд перед применением скина.</red>"));
                        }
                    } else {
                        if (clickedItem.getItemMeta() != null && clickedItem.getItemMeta().getPersistentDataContainer().has(CustomSkin.CLEAR_SKIN_KEY, PersistentDataType.STRING)) {
                            long lastClearTime = lastSkinClearTime.getOrDefault(player.getUniqueId(), 0L);
                            long currentTime = System.currentTimeMillis();
                            if (lastClearTime - currentTime >= 20000){
                                lastSkinClearTime.put(player.getUniqueId(), currentTime);
                                clearSkin(clickedItem, player);

                                event.setCursor(null);
                                event.setCancelled(true);
                            } else {
                                player.sendMessage(MiniMessage.miniMessage().deserialize(" <gray>|</gray> <red>Подождите <bold>20</bold> секунд перед применением скина.</red>"));
                            }
                        }
                    }
                }
            }
        }
    }

    private void clearSkin(ItemStack item, Player player){
        ItemMeta meta = item.getItemMeta();

        if (meta != null && meta.getPersistentDataContainer().has(CustomSkin.APPLIED_SKIN_KEY, PersistentDataType.STRING)){
            meta.setCustomModelData(null);
            giveSkin(item, player);
            meta.getPersistentDataContainer().remove(CustomSkin.APPLIED_SKIN_KEY);
            item.setItemMeta(meta);
        }
    }

    private void giveSkin(ItemStack item, Player player) {
        ItemMeta meta = item.getItemMeta();
        String skinName = meta.getPersistentDataContainer().get(CustomSkin.APPLIED_SKIN_KEY, PersistentDataType.STRING);
        CustomSkin skin = null;

        try {
            skin = CustomSkin.fromFile(CWSkins.getSkinFile(skinName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (skin != null && skinName != null) {
            ItemStack skinItem = new ItemStack(skin.getMaterial());
            ItemMeta skinMeta = skinItem.getItemMeta();
            skinMeta.setDisplayName(skin.getItemName());
            skinMeta.setCustomModelData(skin.getCustomModelData());
            skinMeta.setLore(skin.getLore());
            skinMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            skinMeta.getPersistentDataContainer().set(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING, skinName);
            skinItem.setItemMeta(skinMeta);

            player.getInventory().addItem(skinItem);
        }
    }

}
