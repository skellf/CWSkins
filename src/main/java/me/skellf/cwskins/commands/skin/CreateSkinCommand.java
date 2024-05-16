package me.skellf.cwskins.commands.skin;

import me.skellf.cwskins.CWSkins;
import me.skellf.cwskins.CustomSkin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CreateSkinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("Эта комманда только для игроков!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("cwskins.createskin")){
            player.sendMessage(MiniMessage.miniMessage().deserialize(" <gray>|</gray> <red>У вас нету прав на эту комманду!</red>"));
            return true;
        }

        if (args.length != 1){
            return false;
        }

        String skinName = args[0];
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand.getType() == Material.AIR){
            player.sendMessage(MiniMessage.miniMessage().deserialize(" <gray>|</gray> <red>Вам нужно держать предмет в руке, чтобы создать скин</red>"));
            return true;
        }

        String itemName = itemInHand.getItemMeta().getDisplayName();
        List<String> lore = itemInHand.getItemMeta().getLore();
        Material material = itemInHand.getType();
        int customModelData = itemInHand.getItemMeta().getCustomModelData();

        CustomSkin skin = new CustomSkin(itemName, lore, material, customModelData);

        File skinFile = CWSkins.getSkinFile(skinName);

        try {
            skin.writeToFile(skinFile);
            player.sendMessage(MiniMessage.miniMessage().deserialize(" <gray>|</gray> <green>Скин успешно сохранен!</green>"));
        } catch (IOException e){
            e.printStackTrace();
            player.sendMessage(MiniMessage.miniMessage().deserialize(" <gray>|</gray> <red>Произошло ошибка при сохранении скина.</red>"));
        }

        return true;
    }
}
