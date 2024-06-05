package me.skellf.cwskins.commands.tabcomplete;

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
            return subCommands;
        }
        return null;
    }
}
