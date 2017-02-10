package io.github.kunonx.DesignFramework.command.DesignFramework;

import io.github.kunonx.DesignFramework.command.CustomizeCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class DesignFrameworkCommand extends CustomizeCommand<DesignFrameworkCommand>
{

    @Override
    public boolean perform(CommandSender sender, List<String> args) {
        return false;
    }
}
