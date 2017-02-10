package io.github.kunonx.DesignFramework.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface CommandExecutable
{
    /**
     *
     * The perform function is a function that is executed when the command is run. This is equivalent to the role of OnCommand.
     * This is implemented by overriding it in the subclass. If you don't need to do, you don't have to it.
     *
     * @param sender Source of the command
     * @param args Passed command arguments, List type
     * @see org.bukkit.plugin.java.JavaPlugin#onCommand(CommandSender, Command, String, String[])
     * @see CustomizeCommand#execute(CommandSender, List)
     * @since 1.0.0
     * @author Kunonx
     */
    boolean perform(CommandSender sender, List<String> args);
}
