package io.github.kunonx.DesignFramework;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Parameter
{
    private boolean enabledConsole                                      = true;
    public void setUsingConsole(boolean usable)                         { this.enabledConsole = usable; }
    public boolean UsableConsole()                                      { return this.enabledConsole; }

    private boolean enabledPlayer                                       = true;
    public void setUsingPlayer(boolean usable)                          { this.enabledPlayer = usable; }
    public boolean UsablePlayer()                                       { return this.enabledPlayer; }
    public boolean isAvailable(CommandSender sender)
    {
        if(sender instanceof Player)
            return this.enabledPlayer;
        else if(sender instanceof ConsoleCommandSender)
            return this.enabledConsole;
        else
            return false;
    }

    public Parameter(String param)                                      { this.paramName = param; }
    public Parameter(String param, boolean requirement)                 { this.paramName = param; this.requirement = requirement; }
    public Parameter(String param, boolean requirement, boolean usablePlayer, boolean usableConsole)
    {
        this.paramName = param;
        this.requirement = requirement;
        this.enabledPlayer = usablePlayer;
        this.enabledConsole = usableConsole;
    }

    private String requirementFormat = "&7<%s>";
    public void setRequirementFormat(String format)                     { this.requirementFormat = format; }
    public String getRequirementFormat()                                { return this.requirementFormat; }

    private String optionalFormat = "&8[%s]";
    public void setOptionalFormat(String format)                        { this.requirementFormat = format; }
    public String getOptionalFormat()                                   { return this.requirementFormat; }

    private final String paramName;
    public String getName()                                             { return this.paramName; }
    public String getParamString()                                      { return this.requirement ? String.format(this.requirementFormat, this.paramName) : String.format(this.optionalFormat, this.paramName); }

    private boolean requirement                                         = false;
    public void setRequirement(boolean require)                         { this.requirement = require; }
    public boolean isRequirement()                                      { return this.requirement; }
}