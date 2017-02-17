package io.github.kunonx.DesignFramework.core;

import io.github.kunonx.DesignFramework.ClassActivation;
import io.github.kunonx.DesignFramework.plugin.DesignFrameworkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;

public abstract class Core implements ClassActivation, Listener, Runnable
{
    private static final Set<Core> named_instance = new HashSet<Core>();
    public static Set<Core> getActiveRegister() { return named_instance; }

    private long delay = 0L;
    public long getDelay() { return this.delay; }
    public void setDelay(long delay) { this.delay = delay; }

    private long period = 0L;
    public long getPeriod() { return this.period; }
    public void setPeriod(long period) { this.period = period; }

    private boolean sync = true;
    public boolean isSync() { return this.sync; }
    public void setSync(boolean sync) { this.sync = sync; }

    private BukkitTask task = null;
    public BukkitTask getTask() { return this.task; }
    public int getTaskId() { return this.task == null ? -1 : this.task.getTaskId(); }

    private DesignFrameworkPlugin plugin = null;
    public DesignFrameworkPlugin getActivePlugin() { return this.plugin; }
    public boolean hasActivePlugin() { return this.plugin != null; }
    public void setPlugin(DesignFrameworkPlugin plugin)
    {
        if(this.hasActivePlugin()) return;
        this.plugin = plugin;
    }

    @Override
    public void setEnabled(DesignFrameworkPlugin plugin)
    {
        this.plugin = plugin;
        this.setEnabled(plugin != null);
    }

    @Override
    public void setEnabled(boolean active)
    {
        this.preLoad(active);
        this.loadRegisterListener(active);
        this.setActivationTask(active);
        this.finLoad(active);

        if(active)
        {
            if(! this.isEnabled())
                named_instance.add(this);
        }
        else
        {
            if(this.isEnabled())
                named_instance.remove(this);
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null) return false;
        if(obj instanceof Core)
        {
            Core core = (Core)obj;
            return (core.task == this.task) && (core.getActivePlugin() == this.getActivePlugin());
        }
        return false;
    }

    @Override
    public boolean isEnabled()
    {
        for(Core core : Core.getActiveRegister())
        {
            if(core.equals(this))
            {
                return true;
            }
        }
        return false;
    }

    public void setActivationTask(boolean active)
    {
        if(active)
        {
            if(this.getActivePlugin().isEnabled())
                if(this.isSync())
                {
                    this.task = Bukkit.getScheduler().runTaskTimer(this.getActivePlugin(), this, this.getDelay(), this.getPeriod());
                }
                else
                {
                    this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(this.getActivePlugin(), this, this.getDelay(), this.getPeriod());
                }
        }
        else
        {
            if(this.task != null)
            {
                this.task.cancel();
                this.task = null;
            }
        }
    }

    public void loadRegisterListener(boolean active)
    {
        if(active)
        {
            DesignFrameworkPlugin plugin = this.getActivePlugin();
            if(plugin.isEnabled())
                    Bukkit.getPluginManager().registerEvents(this, this.getActivePlugin());
        }
        else
        {
            HandlerList.unregisterAll(this);
        }
    }

    protected void preLoad(boolean active)
    {
    }


    protected void finLoad(boolean active)
    {

    }

    @Override
    public void run()
    {

    }

    /**
     * Call the action method synchronously.
     */
    public synchronized void sync()
    {
        this.run();
    }
}
