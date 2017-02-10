package io.github.kunonx.DesignFramework.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class CustomizeBukkitCommand extends Command implements PluginIdentifiableCommand
{
    protected CustomizeCommand<?> customizeCommand;
    public CustomizeCommand<?> getCustomizeCommand() { return this.customizeCommand; }

    public CustomizeBukkitCommand(String name, CustomizeCommand<?> command)
    {
        super(name, command.getDescription(), command.getPermissionMessage(), command.getAliases());
        this.customizeCommand = command;
    }

    @Override
    public Plugin getPlugin()
    {
        return this.customizeCommand.getActivePlugin();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args)
    {
        return this.customizeCommand.execute(sender, Arrays.asList(args));
    }
}
