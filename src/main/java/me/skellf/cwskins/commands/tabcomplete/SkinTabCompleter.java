package me.skellf.cwskins.commands.tabcomplete;

import me.skellf.cwskins.CWSkins;
import me.skellf.cwskins.CustomSkin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SkinTabCompleter implements TabCompleter {
    private final List<String> subCommands;

    public SkinTabCompleter() {
        subCommands = new ArrayList<>();
        subCommands.add("menu");
        subCommands.add("reload");
        subCommands.add("give");
        subCommands.add("clear");
        subCommands.add("create");
        subCommands.add("remove");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 1) {
            return getMatchingSubCommands(args[0]);
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            return getMatchingSkins(args[2]);
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            return getMatchingSkins(args[1]);
        }

        return null;
    }

    private List<String> getMatchingSubCommands(String input) {
        List<String> matches = new ArrayList<>();
        for (String subCommand : subCommands) {
            if (subCommand.toLowerCase().startsWith(input.toLowerCase())) {
                matches.add(subCommand);
            }
        }
        return matches;
    }

    private List<String> getMatchingSkins(String input) {
        List<String> matches = new ArrayList<>();
        for (CustomSkin skin : CWSkins.getAllSkins()) {
            String skinName = skin.getFileName();
            if (skinName.toLowerCase().startsWith(input.toLowerCase())) {
                matches.add(skinName);
            }
        }
        return matches;
    }
}