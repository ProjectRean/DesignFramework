package io.github.kunonx.DesignFramework.plugin.config;

import io.github.kunonx.DesignFramework.SyncConfigFileReader;
import io.github.kunonx.DesignFramework.message.Msg;
import io.github.kunonx.DesignFramework.plugin.DesignFrameworkPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.scanner.ScannerException;

import java.io.File;
import java.io.IOException;

public class SyncYamlConfiguration extends SyncConfigFileReader
{
    private YamlConfiguration yaml = null;
    public YamlConfiguration getYaml() { return this.yaml; }
    public void setYaml(YamlConfiguration y) { this.yaml = y; }
    public boolean isYaml() { return this.yaml != null; }

    @Override
    public void refresh()
    {
        try
        {
            this.setYaml(YamlConfiguration.loadConfiguration(this.getFile()));
        }
        catch(ScannerException e)
        {
            Msg.console(this.activePlugin, "&cScanner exception handling has occurred. The file {0} probably has the wrong syntax.", this.getFile().toString());
        }
    }

    private SyncYamlConfiguration()
    {

    }

    private SyncYamlConfiguration(DesignFrameworkPlugin plugin)
    {
        this.setYaml(YamlConfiguration.loadConfiguration(plugin.getConfigFile()));
        this.file = plugin.getConfigFile();
        this.setEnabled(plugin);
    }

    private SyncYamlConfiguration(DesignFrameworkPlugin plugin, File file)
    {
        this.setYaml(YamlConfiguration.loadConfiguration(file));
        this.file = file;
        this.setEnabled(plugin);
    }

    public static SyncYamlConfiguration initLoad(DesignFrameworkPlugin plugin)
    {
        if(plugin == null) return null;
        for(Object o : SyncConfigFileReader.getRegister())
        {
            if(o instanceof SyncYamlConfiguration)
            {
                SyncYamlConfiguration d = (SyncYamlConfiguration)o;
                if(d.getActivePlugin() == plugin && d.getFile() == null)
                {
                    return d;
                }

            }
        }
        return new SyncYamlConfiguration(plugin);
    }

    public static SyncYamlConfiguration initLoad(DesignFrameworkPlugin plugin, File customFile)
    {
        if(customFile == null) return null;
        for(Object o : SyncConfigFileReader.getRegister())
        {
            if(o instanceof SyncYamlConfiguration)
            {
                SyncYamlConfiguration d = (SyncYamlConfiguration)o;
                if(d.getFile() == customFile && d.getActivePlugin() == plugin)
                {
                    return d;
                }

            }
        }
        return new SyncYamlConfiguration(plugin, customFile);
    }

    public static SyncYamlConfiguration initLoad(DesignFrameworkPlugin plugin, String customFile)
    {
        if(customFile == null) return null;
        for(Object o : SyncConfigFileReader.getRegister())
        {
            if(o instanceof SyncYamlConfiguration)
            {
                SyncYamlConfiguration d = (SyncYamlConfiguration)o;
                if(d.getFile() == new File(customFile))
                {
                    return d;
                }
            }
        }

        return new SyncYamlConfiguration(plugin, new File(customFile));
    }

    public boolean containsKey(String path)
    {
        return this.yaml.contains(path);
    }

    public void fixedDefault(String key, Object value)
    {
        if(! this.yaml.contains(key))
        {
            this.yaml.set(key, value);
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null) return false;
        if(obj instanceof YamlConfiguration)
        {
            return obj.equals(this.getYaml());
        }
        else if(obj instanceof SyncYamlConfiguration)
        {
            return ((SyncYamlConfiguration) obj).getFile() == this.getFile();
        }
        return false;
    }

    public boolean contains(String... keys)
    {
        String key = null;
        for(String k : keys)
        {
            if(key == null)
            {
                key = k;
                continue;
            }

            key = key + "." + k;
        }
        return this.yaml.contains(key);
    }

    public Object get(String... keys)
    {
        String key = null;
        for(String k : keys)
        {
            if(key == null)
            {
                key = k;
                continue;
            }

            key = key + "." + k;
        }
        return this.yaml.get(key);
    }

    public String getString(String... keys)
    {
        String key = null;
        for(String k : keys)
        {
            if(key == null)
            {
                key = k;
                continue;
            }
            key = key + "." + k;
        }
        return this.yaml.getString(key);
    }

    public double getDouble(String... keys)
    {
        String key = null;
        for(String k : keys)
        {
            if(key == null)
            {
                key = k;
                continue;
            }
            key = key + "." + k;
        }
        return this.yaml.getDouble(key);
    }

    public boolean getBoolean(String... keys)
    {
        String key = null;
        for(String k : keys)
        {
            if(key == null)
            {
                key = k;
                continue;
            }
            key = key + "." + k;
        }
        return this.yaml.getBoolean(key);
    }

    public float getFloat(String... keys)
    {
        String key = null;
        for(String k : keys)
        {
            if(key == null)
            {
                key = k;
                continue;
            }
            key = key + "." + k;
        }
        return (float)this.yaml.getDouble(key);
    }

    public void removeKey(String key)
    {
        System.out.println(this.yaml.getCurrentPath());
        if(this.yaml.contains(key))
        {
            this.yaml.set(key, null);
        }
    }

    public boolean save()
    {
        try
        {
            this.yaml.save(this.getFile());
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
