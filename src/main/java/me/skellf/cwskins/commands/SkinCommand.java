package me.skellf.cwskins.commands;

import me.skellf.cwskins.CWSkins;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class SkinCommand {

    protected final CWSkins plugin = CWSkins.getInstance();
    protected final MiniMessage mm = MiniMessage.miniMessage();

    public abstract boolean execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);

}
