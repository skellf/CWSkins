package me.skellf.cwskins.commands.skin;

import me.skellf.cwskins.CWSkins;
import me.skellf.cwskins.CustomSkin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class GiveSkinCommand implements CommandExecutor {

    MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length != 2){
            return false;
        }

        if (!sender.hasPermission("cwskins.giveskin")){
            sender.sendMessage(mm.deserialize(CWSkins.getInstance().getMessage("noPermission")));
            return true;
        }

        String playerName = args[0];
        String skinName = args[1];

        Player player = Bukkit.getPlayer(playerName);
        if (player == null || !player.isOnline()){
            sender.sendMessage(mm.deserialize(CWSkins.getInstance().getMessage("giveskincommand.playerNotFound", playerName)));
            return true;
        }

        CustomSkin skin;
        try {
            skin = CustomSkin.fromFile(CWSkins.getSkinFile(skinName));
        } catch (IOException e){
            e.printStackTrace();
            sender.sendMessage(mm.deserialize(CWSkins.getInstance().getMessage("errorOccurred")));
            return true;
        }

        if (skin == null){
            sender.sendMessage(mm.deserialize(CWSkins.getInstance().getMessage("giveskincommand.skinNotFound", skinName)));
            return true;
        }

        ItemStack skinItem = new ItemStack(skin.getMaterial());
        ItemMeta meta = skinItem.getItemMeta();
        meta.setDisplayName(skin.getItemName());
        meta.setCustomModelData(skin.getCustomModelData());
        meta.setLore(skin.getLore());
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(CustomSkin.CUSTOM_SKIN_KEY, PersistentDataType.STRING, skinName);
        skinItem.setItemMeta(meta);

        player.getInventory().addItem(skinItem);

        sender.sendMessage(mm.deserialize(CWSkins.getInstance().getMessage("giveskincommand.skinGivenSuccessfully")));

        return true;
    }
}
