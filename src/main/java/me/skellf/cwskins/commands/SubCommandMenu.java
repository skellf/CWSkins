package me.skellf.cwskins.commands;

import me.skellf.cwskins.SkinsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubCommandMenu extends SkinCommand {

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(mm.deserialize(plugin.getMessage("commandForPlayers")));
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("cwskins.menu")) {
            player.sendMessage(mm.deserialize(plugin.getMessage("noPermission")));
            return true;
        }

        new SkinsMenu().displayTo(player);
        return true;
    }
}
