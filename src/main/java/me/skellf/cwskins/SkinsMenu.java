package me.skellf.cwskins;

import me.skellf.cwskins.util.ColorTranslate;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.mineacademy.fo.menu.MenuPagged;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SkinsMenu extends MenuPagged<CustomSkin> {

    public SkinsMenu() {
        super(calculateInventorySize(getAllSkins().size()), getAllSkins());
        this.addInfoButton();
        this.addPageNumbers();
        this.addReturnButton();

        this.setTitle(CWSkins.getInstance().getMessage("skins.Menu"));
    }

    private static List<CustomSkin> getAllSkins() {
        List<CustomSkin> skins = new ArrayList<>();
        File skinsFolder = new File(CWSkins.getInstance().getDataFolder(), "skins");
        if (skinsFolder.exists() && skinsFolder.isDirectory()) {
            for (File skinFile : skinsFolder.listFiles()) {
                try {
                    CustomSkin skin = CustomSkin.fromFile(skinFile);
                    if (skin != null) {
                        skins.add(skin);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return skins;
    }

    private static int calculateInventorySize(int itemCount) {
        int size = ((itemCount / 9) + 1) * 9;
        return size > 54 ? 54 : size;
    }

    @Override
    protected ItemStack convertToItemStack(CustomSkin skin) {
        Material material = skin.getMaterial() != null ? skin.getMaterial() : Material.BARRIER;
        String itemName = skin.getItemName() != null ? skin.getItemName() : CWSkins.getInstance().getMessage("unnamedSkin");
        List<String> lore = new ArrayList<>(skin.getLore() != null ? skin.getLore() : new ArrayList<>());
        lore.add("");
        lore.add(ColorTranslate.translateColorCodes("&e" + skin.getSkinFile().getName()));
        int customModelData = skin.getCustomModelData();

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(itemName);
            meta.setCustomModelData(customModelData);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.getPersistentDataContainer().set(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING, skin.getFileName());
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    @Override
    protected void onPageClick(Player player, CustomSkin skin, ClickType click) {
        try {
            giveSkinToPlayer(player, skin.getSkinFile().getName().replace(".yml", ""));
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(MiniMessage.miniMessage().deserialize(CWSkins.getInstance().getMessage("errorOccurred")));
        }
    }

    private void giveSkinToPlayer(Player player, String skinFileName) throws IOException {
        File skinFile = new File(CWSkins.getInstance().getDataFolder(), "skins/" + skinFileName + ".yml");
        CustomSkin skin = CustomSkin.fromFile(skinFile);
        if (skin == null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(CWSkins.getInstance().getMessage("skin.notFound")));
            return;
        }

        Material material = skin.getMaterial() != null ? skin.getMaterial() : Material.BARRIER;
        ItemStack skinItem = new ItemStack(material);
        ItemMeta meta = skinItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(skin.getItemName());
            meta.setCustomModelData(skin.getCustomModelData());
            meta.setLore(skin.getLore());
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.getPersistentDataContainer().set(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING, skinFileName);
            skinItem.setItemMeta(meta);
        }

        player.getInventory().addItem(skinItem);
        player.updateInventory();
    }
}

