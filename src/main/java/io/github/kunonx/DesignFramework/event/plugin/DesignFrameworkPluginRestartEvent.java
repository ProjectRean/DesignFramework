package io.github.kunonx.DesignFramework.event.plugin;

import io.github.kunonx.DesignFramework.plugin.DesignFrameworkPlugin;

/**
 * Created by GIGABYTE on 2017-01-25.
 */
public class DesignFrameworkPluginRestartEvent extends DesignFrameworkPluginEvent
{
    public DesignFrameworkPluginRestartEvent(DesignFrameworkPlugin plugin)
    {
        super(plugin);
    }
}
