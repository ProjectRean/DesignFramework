package io.github.kunonx.DesignFramework.plugin;

import io.github.kunonx.DesignFramework.ClassActivation;
import io.github.kunonx.DesignFramework.accessor.PluginAccessor;
import io.github.kunonx.DesignFramework.command.CustomizeCommand;
import io.github.kunonx.DesignFramework.event.plugin.DesignFrameworkPluginDisableEvent;
import io.github.kunonx.DesignFramework.event.plugin.DesignFrameworkPluginEnableEvent;
import io.github.kunonx.DesignFramework.event.plugin.DesignFrameworkPluginReloadEvent;
import io.github.kunonx.DesignFramework.event.plugin.DesignFrameworkPluginRestartEvent;
import io.github.kunonx.DesignFramework.message.Msg;
import io.github.kunonx.DesignFramework.message.Prefix;
import io.github.kunonx.DesignFramework.message.StringUtil;
import io.github.kunonx.DesignFramework.plugin.config.LangConfiguration;
import io.github.kunonx.DesignFramework.plugin.config.SyncYamlConfiguration;
import io.github.kunonx.DesignFramework.util.ReflectionUtil;
import io.github.kunonx.DesignFramework.util.SystemUtil;

import org.bukkit.Bukkit;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * The skeleton of the DesignFramework plugin. This has further complemented the functionality of the {@link org.bukkit.plugin.java.JavaPlugin}.
 * You can indirectly refer to methods using {@link PluginAccessor}.
 *
 * @version 1.0
 * @author Kunonx
 * @see PluginAccessor
 */
public abstract class DesignFrameworkPlugin extends JavaPlugin implements PluginAccessor
{
    private long SystemEnabledMillis;

    private long SystemDisabledMillis;

    private PluginAccessor accessor;

    private String locale;

    protected Prefix prefix;

    private Msg msg;

    protected LangConfiguration lang;

    private SyncYamlConfiguration yaml;

    public String getLocale() { return this.locale; }

    public long getPluginEnabledMills() { return this.SystemEnabledMillis; }

    public long getPluginDisabledMills() { return this.SystemDisabledMillis; }

    public Prefix getPrefix() { return this.prefix; }

    public Msg getPluginMsg() { return this.msg; }

    protected void setPluginMsg(Msg msg)
    {
        this.getPluginMsg().sendToConsole("&fChanged to custom plugin prefix: {0}", msg.getPrefix());
        this.msg = msg;
        this.prefix = new Prefix(msg.getPrefix());
    }

    public LangConfiguration getLangConfiguration() { return lang; }

    public SyncYamlConfiguration getSyncConfig() { return yaml; }

    public PluginAccessor getPluginAccessor() { return accessor; }

    public void setLanguage(String locale)
    {
        this.locale = locale;

        // Unloading existed LangConfiguration
        this.lang.setEnabled(false);

        // reloading LangConfiguration
        this.lang = new LangConfiguration().load(this);
    }

    @Override
    public void onEnable()
    {
        this.SystemEnabledMillis = System.currentTimeMillis();
        this.accessor = this;
        this.prefix = new Prefix(StringUtil.getColorHash(this.getDescription().getName()) + "[" + this.getDescription().getName() + "] ");
        this.msg = new Msg(this.prefix);

        lang = new LangConfiguration().load(this);
        yaml = SyncYamlConfiguration.initLoad(this);

        this.getPluginMsg().sendToConsole("&b" + this.getDescription().getName() + " v" + this.getDescription().getVersion() + " started by DesignFrameworkPlugin Accessor");
        try
        {
            this.getPluginMsg().sendToConsole("&aCalling initial activation method...");
            this.preLoad();
            synchronized (this)
            {
                // Call method when the plugin is never used or first enabling
                if (this.isNeverEnabled())
                    this.onEnableInitializing();
                DesignFrameworkPluginEnableEvent event = new DesignFrameworkPluginEnableEvent(this);
                event.run();
                this.getPluginMsg().sendToConsole("&aCalling customize activation method of enabling inner...");

                // The method call overridden in the parent class
                event.getPlugin().onEnableInner();
            }
            this.finLoad();
            this.getPluginMsg().sendToConsole("&f" + this.getDescription().getName() + " v" + this.getDescription().getVersion() + " was &eenabled successfully. &7(" +
                    String.valueOf(System.currentTimeMillis() - this.getPluginEnabledMills()) + "ms)");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            this.getPluginMsg().sendToConsole("&f" + this.getDescription().getName() + " v" + this.getDescription().getVersion() + " was &cenabled unsuccessfully. Check your plugin data. &7(" +
                    String.valueOf(System.currentTimeMillis() - this.getPluginEnabledMills()) + "ms)");
        }
    }

    @Override
    public void onDisable()
    {
        try {
            this.SystemDisabledMillis = System.currentTimeMillis();
            this.getPluginMsg().sendToConsole("&c" + this.getDescription().getName() + " v" + this.getDescription().getVersion() + " stopped by DesignFrameworkPlugin Accessor");
            synchronized (this) {
                DesignFrameworkPluginDisableEvent event = new DesignFrameworkPluginDisableEvent(this);
                event.run();
                this.getPluginMsg().sendToConsole("&aCalling customize activation method of disabling inner...");

                // The method call overridden in the parent class
                event.getPlugin().onDisableInner();
            }
            this.getPluginMsg().sendToConsole("&f" + this.getDescription().getName() + " v" + this.getDescription().getVersion() + " was &edisabled successfully. &7(" +
                    String.valueOf(System.currentTimeMillis() - this.getPluginDisabledMills()) + "ms)");
        }
        catch(NoClassDefFoundError e)
        {

        }
    }

    private void onEnableInitializing()
    {
        this.getDataFolder().mkdir();
        this.onEnableFirst();
    }

    public File getConfigFile()
    {
        return new File(this.getDataFolder(), "config.yml");
    }

    protected void onEnableFirst() {}

    protected void preLoad() {}

    protected void finLoad() {}

    protected abstract void onEnableInner();

    protected abstract void onDisableInner();

    @Override
    public synchronized void reload()
    {
        DesignFrameworkPluginReloadEvent event = new DesignFrameworkPluginReloadEvent(this);
        event.run();
        if(event.isCancelled()) return;
        if(event.getPlugin().isEnabled())
        {
            event.getPlugin().onDisable();
        }
        event.getPlugin().onEnable();
    }

    @Override
    public void restart()
    {
        DesignFrameworkPluginRestartEvent event = new DesignFrameworkPluginRestartEvent(this);
        event.run();
        if(event.isCancelled()) return;
        synchronized (this)
        {
            if (this.isEnabled())
            {
                Bukkit.getPluginManager().callEvent(new PluginDisableEvent(event.getPlugin()));
                Bukkit.getPluginManager().disablePlugin(event.getPlugin());
            }
        }
        Bukkit.getPluginManager().callEvent(new PluginEnableEvent(event.getPlugin()));
        Bukkit.getPluginManager().enablePlugin(event.getPlugin());
    }

    @Override
    public boolean isDisabled() { return !this.isEnabled(); }

    @Override
    public boolean isNeverEnabled()  { return !this.getDataFolder().exists(); }

    /*
     * Register the native library with the plugin.
     * @deprecated The method is not validated for safety
     * @param filename Name of file to register
    public void registerNativeModule(String filename)
    {
        try
        {
            filename = SystemUtil.getNativeInterfaceExtensionName(filename);
            File library = new File(this.getDataFolder().getParentFile(), filename);
            Runtime.getRuntime().load(library.getAbsolutePath());
        }
        catch(UnsupportedOperationException e)
        {
            e.printStackTrace();
        }
        catch(UnsatisfiedLinkError e)
        {
            e.printStackTrace();
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
        catch(SecurityException e)
        {

        }
    }
    */

    /*
     * Register the native library with the plugin.
     * @deprecated The method is not validated for safety
     * @param filename Names of all files want to register
    public void registerNativeModule(String... filename)
    {
        for(String name : filename)
        {
            this.registerNativeModule(name);
        }
    }
     */
    @Override
    public void startActivation(Object... objects)
    {
        int i = 0;
        for(Object o : objects)
        {
            if(o instanceof ClassActivation)
            {
                ClassActivation ca = (ClassActivation)o;
                ca.setEnabled(this);
            }
            else
            {
                Class<?> clazz = (Class<?>)o;
                if(! ClassActivation.class.isAssignableFrom(clazz))
                {
                    throw new IllegalArgumentException(clazz.getName() + " isn't ClassActivation class. Ignoring it");
                }
                else
                {
                    Object instance = ReflectionUtil.getInstance(clazz);
                    ClassActivation ca = (ClassActivation)instance;
                    ca.setEnabled(this);
                    if(CustomizeCommand.class.isAssignableFrom(clazz)) i++;
                }
            }
        }
        if(i != 0)
            this.getPluginMsg().sendToConsole("&e{0} Command classes has been registered", i);
        this.getPluginMsg().sendToConsole("&eRegistered class activation " + objects.length + " core(s)");
    }

    @Override
    public void stopActivation(Object... objects)
    {
        for (Object o : objects)
        {
            if (o instanceof ClassActivation)
            {
                ClassActivation ca = (ClassActivation) o;
                ca.setEnabled(false);
            }
            else
            {
                Class<?> clazz = (Class<?>) o;
                if (! ClassActivation.class.isAssignableFrom(clazz))
                {
                    throw new IllegalArgumentException(clazz.getName() + " isn't ClassActivation class. Ignoring it");
                }
                else
                {
                    Object instance = ReflectionUtil.getInstance(clazz);
                    ClassActivation ca = (ClassActivation) instance;
                    ca.setEnabled(false);
                }
            }
        }
    }
}