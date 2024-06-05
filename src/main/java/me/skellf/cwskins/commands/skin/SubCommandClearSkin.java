package me.skellf.cwskins.commands.skin;

import me.skellf.cwskins.CustomSkin;
import me.skellf.cwskins.commands.SkinCommand;
import me.skellf.cwskins.util.ColorTranslate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SubCommandClearSkin extends SkinCommand {

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("cwskins.clearskin")){
            sender.sendMessage(mm.deserialize(plugin.getMessage("noPermission")));
            return true;
        }

        if (args.length != 2){
            sender.sendMessage(mm.deserialize(plugin.getMessage("insufficientArguments")));
            return true;
        }

        String playerName = args[1];

        Player player = Bukkit.getPlayer(playerName);
        if (player == null || !player.isOnline()){
            sender.sendMessage(mm.deserialize(plugin.getMessage("playerNotFound", playerName)));
            return true;
        }


        String displayName = plugin.getConfig().getString("items.clearSkinItem.name");
        List<String> lore = plugin.getConfig().getStringList("items.clearSkinItem.lore");
        String materialName = plugin.getConfig().getString("items.clearSkinItem.material");

        Material material = Material.valueOf(materialName);

        ItemStack clearSkinItem = new ItemStack(material);
        ItemMeta meta = clearSkinItem.getItemMeta();

        if (displayName != null){
            displayName = ColorTranslate.translateColorCodes(displayName);
            meta.setDisplayName(displayName);
        }

        List<String> translatedLore = new ArrayList<>();
        for (String line : lore){
            translatedLore.add(ColorTranslate.translateColorCodes(line));
        }

        meta.setLore(translatedLore);
        meta.getPersistentDataContainer().set(CustomSkin.CLEAR_SKIN_KEY, PersistentDataType.STRING, "clear");
        clearSkinItem.setItemMeta(meta);

        if (player != null){
            player.getInventory().addItem(clearSkinItem);
        }

        return true;
    }
}
