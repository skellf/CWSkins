package me.skellf.cwskins.commands.skin;

import me.skellf.cwskins.CWSkins;
import me.skellf.cwskins.CustomSkin;
import me.skellf.cwskins.commands.SkinCommand;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SubCommandCreateSkin extends SkinCommand {

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("cwskins.createskin")){
            sender.sendMessage(mm.deserialize(plugin.getMessage("noPermission")));
            return true;
        }


        if (!(sender instanceof Player)){
            sender.sendMessage(plugin.getMessage("commandForPlayers"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 2){
            return false;
        }

        String skinName = args[1];
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand.getType() == Material.AIR){
            player.sendMessage(mm.deserialize(plugin.getMessage("createskincommand.noItemInHand")));
            return true;
        }

        String itemName = itemInHand.getItemMeta().getDisplayName();
        List<String> lore = itemInHand.getItemMeta().getLore();
        Material material = itemInHand.getType();
        int customModelData = itemInHand.getItemMeta().getCustomModelData();

        if (customModelData == 0){
            player.sendMessage(mm.deserialize(plugin.getMessage("createskincommand.noCustomModelData")));
        }

        File skinFile = CWSkins.getSkinFile(skinName);

        CustomSkin skin = new CustomSkin(itemName, lore, material, customModelData, skinFile);

        try {
            skin.writeToFile(skinFile);
            player.sendMessage(mm.deserialize(plugin.getMessage("createskincommand.skinSaved")));
        } catch (IOException e){
            e.printStackTrace();
            player.sendMessage(mm.deserialize(plugin.getMessage("errorOccurred")));
        }

        return true;
    }
}
