package io.github.kunonx.DesignFramework.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;

public abstract class DesignFrameworkEvent extends Event implements Runnable, Cancellable
{
    private boolean cancel;
    public Map<Object, Object> CustomData = new HashMap<Object, Object>();

    @Override
    public void run()
    {
        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public boolean isCancelled()
    {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel)
    {
        this.cancel = cancel;
    };

    public Map<Object, Object> getCustomData()
    {
        return this.CustomData;
    }

    @SuppressWarnings("unchecked")
    public void setCustomData(Map<?, ?> m)
    {
        this.CustomData = (Map<Object, Object>) m;
    }
}
