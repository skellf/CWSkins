package me.skellf.cwskins.commands.skin;

import me.skellf.cwskins.CWSkins;
import me.skellf.cwskins.CustomSkin;
import me.skellf.cwskins.util.ColorTranslate;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ClearSkinCommand implements CommandExecutor {

    MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1){
            sender.sendMessage(mm.deserialize(CWSkins.getInstance().getMessage("clearkincommand.noPlayerName")));
            return true;
        }

        if (!sender.hasPermission("cwskins.clearskin")){
            sender.sendMessage(mm.deserialize(CWSkins.getInstance().getMessage("noPermission")));
            return true;
        }

        String playerName = args[0];

        Player player = Bukkit.getPlayer(playerName);

        String displayName = CWSkins.getInstance().getConfig().getString("items.clearSkinItem.name");
        List<String> lore = CWSkins.getInstance().getConfig().getStringList("items.clearSkinItem.lore");
        String materialName = CWSkins.getInstance().getConfig().getString("items.clearSkinItem.material");

        Material material = Material.valueOf(materialName);

        ItemStack clearSkinItem = new ItemStack(material);
        ItemMeta meta = clearSkinItem.getItemMeta();

        if (displayName != null){
            displayName = ColorTranslate.translateColorCodes(displayName);
            meta.setDisplayName(displayName);
        }

        List<String> translatedLore = new ArrayList<>();
        for (String line : lore) {
            translatedLore.add(ColorTranslate.translateColorCodes(line));
        }

        meta.setLore(translatedLore);
        meta.getPersistentDataContainer().set(CustomSkin.CLEAR_SKIN_KEY, PersistentDataType.STRING, "clear");
        clearSkinItem.setItemMeta(meta);

        player.getInventory().addItem(clearSkinItem);


        return true;
    }
}
