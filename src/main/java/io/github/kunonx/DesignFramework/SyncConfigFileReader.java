package io.github.kunonx.DesignFramework;

import io.github.kunonx.DesignFramework.message.Msg;
import io.github.kunonx.DesignFramework.message.Msg.MsgLevel;
import io.github.kunonx.DesignFramework.plugin.DesignFrameworkPlugin;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public abstract class SyncConfigFileReader implements ClassActivation
{
    protected static transient final Set<Object> named_instance = new HashSet<Object>();

    public static Set<Object> getRegister()
    {
        return named_instance;
    }

    protected DesignFrameworkPlugin activePlugin;

    public DesignFrameworkPlugin getActivePlugin()
    {
        return this.activePlugin;
    }

    protected File file = null;

    public File getFile()
    {
        return this.file;
    }

    public String getPath()
    {
        return this.file.getPath();
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    public File getFolder()
    {
        return this.file.getParentFile();
    }

    public synchronized void refresh() {
    }

    public boolean createDataFolder(String path)
    {
        if (!new File(activePlugin.getDataFolder(), path).exists()) {
            Msg.log(this.activePlugin, MsgLevel.WARNING, "&c\"" + path + "\" directory not found! Creating the directory");
            new File(activePlugin.getDataFolder(), path).mkdirs();
        }

        if (!new File(activePlugin.getDataFolder(), path).exists()) {
            return false;
        }
        return true;
    }

    public boolean createDataFolders(String... paths)
    {
        for (String path : paths) {
            if (!new File(activePlugin.getDataFolder(), path).exists()) {
                Msg.log(this.activePlugin, MsgLevel.WARNING, "&c\"" + path + "\" directory not found! Creating the directory");
                new File(activePlugin.getDataFolder(), path).mkdirs();
            }

            if (!new File(activePlugin.getDataFolder(), path).exists()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setEnabled(boolean enable)
    {
        if (enable) {
            if (!this.isEnabled())
                named_instance.add(this);
        } else {
            if (this.isEnabled()) {
                named_instance.remove(this);
            }
        }
    }

    @Override
    public void setEnabled(DesignFrameworkPlugin plugin)
    {
        this.activePlugin = plugin;
        this.setEnabled(this.activePlugin != null);
    }

    @Override
    public boolean isEnabled()
    {
        for (Object o : SyncConfigFileReader.getRegister()) {
            if (o instanceof SyncConfigFileReader) {
                SyncConfigFileReader sync = (SyncConfigFileReader) o;
                if (sync.equals(this))
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (obj instanceof SyncConfigFileReader) {
            return this.getFile() == ((SyncConfigFileReader) obj).getFile();
        } else if (obj instanceof File) {
            return this.getFile() == obj;
        }
        return false;
    }
}
