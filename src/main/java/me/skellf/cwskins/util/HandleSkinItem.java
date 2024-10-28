package me.skellf.cwskins.util;

import me.skellf.cwskins.CustomSkin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;

public class HandleSkinItem {

    public static ItemStack createSkinItem(File skinFile) throws IOException{

        CustomSkin skin = CustomSkin.fromFile(skinFile);
        if (skin == null)  {
            throw new IOException("Skin not found");
        }

        String skinName = skin.getFileName();

        ItemStack skinItem = new ItemStack(skin.getMaterial());
        ItemMeta meta = skinItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(skin.getItemName());
            meta.setCustomModelData(skin.getCustomModelData());
            meta.setLore(skin.getLore());
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.getPersistentDataContainer().set(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING, skinName);
            skinItem.setItemMeta(meta);
        }

        return skinItem;
    }

    public static void giveSkinItem(File skinFile, Player player) throws IOException {
        ItemStack skinItem = createSkinItem(skinFile);

        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), skinItem);
            return;
        }

        player.getInventory().addItem(skinItem);
    }
}
