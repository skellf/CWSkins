package me.skellf.cwskins.commands;

import me.skellf.cwskins.CWSkins;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CWSkinsCommand implements CommandExecutor {

    MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!sender.hasPermission("cwskins.reload")){
            sender.sendMessage(mm.deserialize(CWSkins.getInstance().getMessage("noPermission")));
            return true;
        }

        CWSkins.getInstance().loadMessages();
        CWSkins.getInstance().reloadConfig();
        sender.sendMessage(mm.deserialize(CWSkins.getInstance().getMessage("reloadedPlugin")));

        return true;
    }
}
