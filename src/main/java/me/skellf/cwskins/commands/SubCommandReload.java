package me.skellf.cwskins.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SubCommandReload extends SkinCommand {

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("cwskins.reload")) {
            sender.sendMessage(mm.deserialize(plugin.getMessage("noPermission")));
            return true;
        }

        plugin.loadMessages();
        plugin.reloadConfig();
        sender.sendMessage(mm.deserialize(plugin.getMessage("reloadedPlugin")));
        return true;
    }
}
