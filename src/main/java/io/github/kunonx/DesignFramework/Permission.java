package io.github.kunonx.DesignFramework;

import io.github.kunonx.DesignFramework.message.StringUtil;
import io.github.kunonx.DesignFramework.plugin.DesignFrameworkPlugin;

public class Permission
{
    private String permission = null;
    public void setPermission(String arg0) { this.permission = arg0; }
    public String getPermission() { return this.permission; }

    private boolean defaultOp = true;
    public void setDefault(boolean Op) { this.defaultOp = Op; }
    public boolean isDefaultOp() { return this.defaultOp; }

    private String message = "Permission denied.";
    public void setMessage(String arg0) { this.message = StringUtil.Color(arg0); }
    public String getMessage() { return this.message; }

    public Permission(String permission)
    {
        this(permission, true);
    }

    public Permission(String permission, boolean defaultOp)
    {
        this.permission = permission;
        this.defaultOp = defaultOp;
    }

    protected transient DesignFrameworkPlugin activePlugin;
    public DesignFrameworkPlugin getActivePlugin() { return this.activePlugin; }
/*
    @Override
    public void setEnabled(DesignFrameworkPlugin plugin)
    {
        this.activePlugin = plugin;
        this.setEnabled(this.activePlugin != null);
    }

    @Override
    public void setEnabled(boolean active)
    {
        if(active)
        {
            // TODO something...
        }
        else
        {
            // TODO something...
        }
    }

    @Override
    public boolean isEnabled()
    {
        return this.activePlugin != null;
    }*/
}

