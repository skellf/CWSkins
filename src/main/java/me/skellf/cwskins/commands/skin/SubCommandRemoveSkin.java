package me.skellf.cwskins.commands.skin;

import me.skellf.cwskins.CWSkins;
import me.skellf.cwskins.commands.SkinCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class SubCommandRemoveSkin extends SkinCommand {

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("cwskins.removeskin")){
            sender.sendMessage(mm.deserialize(plugin.getMessage("noPermission")));
            return true;
        }

        if (args.length != 2){
            sender.sendMessage(mm.deserialize(plugin.getMessage("insufficientArguments")));
            return true;
        }

        String skinName = args[1];

        File skinFile = CWSkins.getSkinFile(skinName);

        if (!skinFile.exists()){
            sender.sendMessage(mm.deserialize(plugin.getMessage("skinNotFound", skinName)));
            return true;
        }

        try {
            if (skinFile.delete()){
                sender.sendMessage(mm.deserialize(plugin.getMessage("removeskincommand.successfullyRemovedSkin")));
            } else {
                throw new IOException("Failed to delete the skin file.");
            }
        } catch (IOException e){
            sender.sendMessage(mm.deserialize(plugin.getMessage("errorOccurred")));
            e.printStackTrace();
        }

        return true;
    }
}
