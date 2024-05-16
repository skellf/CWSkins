package me.skellf.cwskins.commands.skin;

import me.skellf.cwskins.CustomSkin;
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
            sender.sendMessage(mm.deserialize("<red>Введите имя игрока!</red>"));
            return true;
        }

        String playerName = args[0];

        Player player = Bukkit.getPlayer(playerName);

        if (!player.hasPermission("cwskins.clearskin")){
            player.sendMessage(mm.deserialize(" <gray>|</gray> <red>У вас нет разрешения на эту комманду!</red>"));
            return true;
        }

        List<String> lore = new ArrayList<>();

        ItemStack clearSkinItem = new ItemStack(Material.CLAY_BALL);
        ItemMeta meta = clearSkinItem.getItemMeta();
        meta.setDisplayName("§x§D§1§6§C§1§3Снятие скина");
        lore.add("");
        lore.add("§8▪ §fПеретащите этот предмет");
        lore.add("  §fна скин, чтобы снять его");
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(CustomSkin.CLEAR_SKIN_KEY, PersistentDataType.STRING, "clear");
        clearSkinItem.setItemMeta(meta);

        player.getInventory().addItem(clearSkinItem);


        return true;
    }
}
