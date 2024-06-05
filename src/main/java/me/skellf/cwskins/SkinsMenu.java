package me.skellf.cwskins;

import me.skellf.cwskins.util.ColorTranslate;
import me.skellf.cwskins.util.HandleSkinItem;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SkinsMenu extends MenuPagged<CustomSkin> {

    public SkinsMenu() {
        super(45, getAllSkins());

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
            player.sendMessage(MiniMessage.miniMessage().deserialize(CWSkins.getInstance().getMessage("skins.Menu.notFound")));
            return;
        }

        ItemStack skinItem = null;
        try {
            skinItem = HandleSkinItem.createSkinItem(skinFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (skinItem == null){
            return;
        }

        player.getInventory().addItem(skinItem);
        player.updateInventory();
    }


    @Override
    protected boolean canShowPreviousButton(){
        return getCurrentPage() > 1;
    }

    @Override
    protected boolean canShowNextButton() {
        return getCurrentPage() < getPages().size();
    }

    @Override
    protected int getPreviousButtonPosition() {
        return this.getSize() - 9 + 3;
    }

    @Override
    protected int getNextButtonPosition() {
        return this.getSize() - 9 + 5;
    }

    @Override
    public Button formPreviousButton(){
        if (getCurrentPage() <= 1) {
            return Button.makeEmpty();
        }

        return new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                setCurrentPage(Math.max(getCurrentPage() - 1, 1));
            }

            @Override
            public ItemStack getItem() {

                ItemCreator item = ItemCreator.of(CompMaterial.valueOf(CWSkins.getInstance().getConfig().getString("menu.backButton.material")))
                        .name(CWSkins.getInstance().getConfig().getString("menu.backButton.displayName"));

                int customModelData = CWSkins.getInstance().getConfig().getInt("menu.backButton.customModelData", -1);
                if (customModelData != -1){
                    item.modelData(customModelData);
                }

                return item.make();
            }
        };
    }

    @Override
    public Button formNextButton() {
        if (getCurrentPage() >= getPages().size()) {
            return Button.makeEmpty();
        }

        return new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                setCurrentPage(Math.min(getCurrentPage() + 1, getPages().size()));
            }

            @Override
            public ItemStack getItem() {
                ItemCreator item = ItemCreator.of(CompMaterial.valueOf(CWSkins.getInstance().getConfig().getString("menu.nextButton.material")))
                        .name(CWSkins.getInstance().getConfig().getString("menu.nextButton.displayName"));

                int customModelData = CWSkins.getInstance().getConfig().getInt("menu.nextButton.customModelData", -1);
                if (customModelData != -1){
                    item.modelData(customModelData);
                }

                return item.make();
            }
        };
    }
}

