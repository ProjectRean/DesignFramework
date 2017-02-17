package io.github.kunonx.DesignFramework.event.plugin;

import io.github.kunonx.DesignFramework.event.DesignFrameworkEvent;
import io.github.kunonx.DesignFramework.plugin.DesignFrameworkPlugin;
import org.bukkit.event.HandlerList;

/**
 * Created by GIGABYTE on 2017-01-25.
 */
public class DesignFrameworkPluginEvent extends DesignFrameworkEvent
{
    private DesignFrameworkPlugin plugin;
    public DesignFrameworkPlugin getPlugin() { return this.plugin; }
    public void setPlugin(DesignFrameworkPlugin plugin ) { this.plugin = plugin; }

    public DesignFrameworkPluginEvent(DesignFrameworkPlugin plugin)
    {
        this.plugin = plugin;
    }

    private static final HandlerList Handler = new HandlerList();

    @Override public HandlerList getHandlers() { return Handler; }

    public HandlerList getHandlerList() { return getHandlers(); }

}
