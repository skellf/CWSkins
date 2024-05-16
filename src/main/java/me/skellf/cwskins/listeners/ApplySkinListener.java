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
        ItemStack cursorItem = event.getCursor();
        ItemStack clickedItem = event.getCurrentItem();
        if (cursorItem != null && clickedItem != null && event.getClickedInventory() != null && cursorItem.getType() != Material.AIR && clickedItem.getType() != Material.AIR) {
            ItemMeta cursorMeta = cursorItem.getItemMeta();
            if (cursorMeta != null && cursorMeta.getPersistentDataContainer().has(CustomSkin.CLEAR_SKIN_KEY, PersistentDataType.STRING)) {
                if (!clickedItem.getItemMeta().getPersistentDataContainer().has(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING) && clickedItem.getItemMeta().getPersistentDataContainer().has(CustomSkin.APPLIED_SKIN_KEY)) {
                    Player player = (Player) event.getWhoClicked();
                    long lastClearTime = lastSkinClearTime.getOrDefault(player.getUniqueId(), 0L);
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastClearTime >= 20000) {
                        clearSkin(clickedItem, player, cursorItem);
                        event.setCancelled(true);
                    } else {
                        player.sendMessage(MiniMessage.miniMessage().deserialize(" <gray>|</gray> <red>Подождите <bold>20</bold> секунд перед очисткой скина.</red>"));
                    }
                    return;
                }
            }

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
                    Player player = (Player) event.getWhoClicked();
                    if (!clickedItem.getItemMeta().getPersistentDataContainer().has(CustomSkin.APPLIED_SKIN_KEY, PersistentDataType.STRING) && !clickedItem.getItemMeta().getPersistentDataContainer().has(CustomSkin.CLEAR_SKIN_KEY)) {
                        if (!clickedItem.getItemMeta().getPersistentDataContainer().has(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING)) {
                            UUID playerId = event.getWhoClicked().getUniqueId();
                            long lastApplyTime = lastSkinApplyTime.getOrDefault(playerId, 0L);
                            long currentTime = System.currentTimeMillis();
                            if (currentTime - lastApplyTime >= 5000) {
                                applySkin(clickedItem, skin, skinName, player);
                                event.setCursor(null);
                                event.setCancelled(true);
                            } else {
                                player.sendMessage(MiniMessage.miniMessage().deserialize(" <gray>|</gray> <red>Подождите <bold>5</bold> секунд перед применением скина.</red>"));
                            }
                        } else {
                            player.sendMessage(MiniMessage.miniMessage().deserialize(" <gray>|</gray> <red>На этом предмете уже применен другой скин.</red>"));
                        }
                    }
                    return;
                }
            }

            ItemMeta clickedMeta = clickedItem.getItemMeta();
            if (clickedMeta != null && clickedMeta.getPersistentDataContainer().has(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING)) {
                String skinName = clickedMeta.getPersistentDataContainer().get(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING);
                CustomSkin skin;
                try {
                    skin = CustomSkin.fromFile(CWSkins.getSkinFile(skinName));
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                if (skin != null && skinName != null) {
                    Player player = (Player) event.getWhoClicked();
                    if (clickedItem.getType() == skin.getMaterial() && !clickedItem.getItemMeta().hasCustomModelData() && !clickedItem.getItemMeta().getPersistentDataContainer().has(CustomSkin.APPLIED_SKIN_KEY, PersistentDataType.STRING)) {
                        UUID playerId = event.getWhoClicked().getUniqueId();
                        long lastApplyTime = lastSkinApplyTime.getOrDefault(playerId, 0L);
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - lastApplyTime >= 5000) {
                            applySkin(clickedItem, skin, skinName, player);
                            event.setCancelled(true);
                        } else {
                            player.sendMessage(MiniMessage.miniMessage().deserialize(" <gray>|</gray> <red>Подождите <bold>5</bold> секунд перед применением скина.</red>"));
                        }
                    }
                }
            }
        }
    }


    private void clearSkin(ItemStack item, Player player, ItemStack cursor) {
        lastSkinClearTime.put(player.getUniqueId(), System.currentTimeMillis());
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.getPersistentDataContainer().has(CustomSkin.APPLIED_SKIN_KEY, PersistentDataType.STRING) && !meta.getPersistentDataContainer().has(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING)) {
            String skinName = meta.getPersistentDataContainer().get(CustomSkin.APPLIED_SKIN_KEY, PersistentDataType.STRING);
            CustomSkin skin = null;

            try {
                skin = CustomSkin.fromFile(CWSkins.getSkinFile(skinName));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (skin != null && skinName != null) {
                meta.setCustomModelData(null);
                giveSkin(skinName, player);
                meta.getPersistentDataContainer().remove(CustomSkin.APPLIED_SKIN_KEY);
                item.setItemMeta(meta);
            }

            if (cursor.getAmount() > 1) {
                cursor.setAmount(cursor.getAmount() - 1);
            } else {
                player.setItemOnCursor(null);
            }
        }
    }

    private void giveSkin(String skinName, Player player) {
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

    private void applySkin(ItemStack item, CustomSkin skin, String skinName, Player player) {
        if (item.getType() != skin.getMaterial()) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(" <gray>|</gray> <red>Нельзя применить этот скин на данный предмет.</red>"));
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null && !meta.getPersistentDataContainer().has(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING)) {
            meta.setCustomModelData(skin.getCustomModelData());
            meta.getPersistentDataContainer().set(CustomSkin.APPLIED_SKIN_KEY, PersistentDataType.STRING, skinName);
            item.setItemMeta(meta);
            lastSkinApplyTime.put(player.getUniqueId(), System.currentTimeMillis());

            player.setItemOnCursor(null);
        } else {
            player.sendMessage(MiniMessage.miniMessage().deserialize(" <gray>|</gray> <red>На этом предмете уже применен скин или уже применен другой скин.</red>"));
        }
    }
}
