package io.github.kunonx.DesignFramework.event.entity;

import io.github.kunonx.DesignFramework.entity.world.WorldLocation;
import io.github.kunonx.DesignFramework.event.DesignFrameworkEvent;
import org.bukkit.command.CommandSender;

public class WorldLocationCreateEvent extends DesignFrameworkEvent
{
    private CommandSender creator;
    private WorldLocation location;

    public WorldLocationCreateEvent(CommandSender sender, WorldLocation location)
    {
        this.creator = sender;
        this.location = location;
    }

    public CommandSender getCreator()
    {
        return this.creator;
    }

    public WorldLocation getLocation()
    {
        return this.location;
    }
}
