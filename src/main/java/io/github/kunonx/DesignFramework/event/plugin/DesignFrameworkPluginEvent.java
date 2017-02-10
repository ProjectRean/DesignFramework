package io.github.kunonx.DesignFramework.event.plugin;

import io.github.kunonx.DesignFramework.event.DesignFrameworkEvent;
import io.github.kunonx.DesignFramework.plugin.DesignFrameworkPlugin;

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
}
