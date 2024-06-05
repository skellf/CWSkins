package me.skellf.cwskins.commands.skin;

import me.skellf.cwskins.CWSkins;
import me.skellf.cwskins.CustomSkin;
import me.skellf.cwskins.commands.SkinCommand;
import me.skellf.cwskins.util.HandleSkinItem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class SubCommandGiveSkin extends SkinCommand {

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("cwskins.giveskin")) {
            sender.sendMessage(mm.deserialize(plugin.getMessage("noPermission")));
            return true;
        }

        if (args.length != 3) {
            return false;
        }


        String playerName = args[1];
        String skinName = args[2];

        Player player = Bukkit.getPlayer(playerName);
        if (player == null || !player.isOnline()) {
            sender.sendMessage(mm.deserialize(plugin.getMessage("playerNotFound", playerName)));
            return true;
        }

        File skinFile = CWSkins.getSkinFile(skinName);

        if (!skinFile.exists()) {
            sender.sendMessage(mm.deserialize(plugin.getMessage("skinNotFound", skinName)));
            return true;
        }

        CustomSkin skin;
        try {
            skin = CustomSkin.fromFile(skinFile);
        } catch (IOException e) {
            e.printStackTrace();
            sender.sendMessage(mm.deserialize(plugin.getMessage("errorOccurred")));
            return true;
        }


        try {
            ItemStack skinItem = HandleSkinItem.createSkinItem(skinFile);
            player.getInventory().addItem(skinItem);
            sender.sendMessage(mm.deserialize(CWSkins.getInstance().getMessage("giveskincommand.skinGivenSuccessfully", skinName)));
        } catch (IOException e) {
            e.printStackTrace();
            sender.sendMessage(mm.deserialize(CWSkins.getInstance().getMessage("errorOccurred")));
        }

        return true;
    }
}
