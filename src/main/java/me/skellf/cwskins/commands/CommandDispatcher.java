package me.skellf.cwskins.commands;

import me.skellf.cwskins.CWSkins;
import me.skellf.cwskins.commands.skin.SubCommandClearSkin;
import me.skellf.cwskins.commands.skin.SubCommandCreateSkin;
import me.skellf.cwskins.commands.skin.SubCommandGiveSkin;
import me.skellf.cwskins.commands.skin.SubCommandRemoveSkin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CommandDispatcher implements CommandExecutor {

    private final Map<String, SkinCommand> subCommands = new HashMap<>();

    public CommandDispatcher(){
        subCommands.put("reload", new SubCommandReload());
        subCommands.put("menu", new SubCommandMenu());
        subCommands.put("clear", new SubCommandClearSkin());
        subCommands.put("create", new SubCommandCreateSkin());
        subCommands.put("give", new SubCommandGiveSkin());
        subCommands.put("remove", new SubCommandRemoveSkin());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(CWSkins.getInstance().getMessage("insufficientArguments")));
            return true;
        }

        SkinCommand subCommand = subCommands.get(args[0].toLowerCase());
        if (subCommand != null){
            return subCommand.execute(sender, command, s, args);
        } else {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(CWSkins.getInstance().getMessage("unknownArgument")));
            return true;
        }
    }
}
