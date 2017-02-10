package io.github.kunonx.DesignFramework.message;

import io.github.kunonx.DesignFramework.security.IntegrityChecker;
import org.bukkit.ChatColor;

import java.security.NoSuchAlgorithmException;

public class Prefix
{
    private String name = null;
    public boolean hasName() { return this.name != null; }
    public boolean hasColor() { return name.equalsIgnoreCase(ChatColor.stripColor(name)); }
    public String getName() { return this.name; }

    public Prefix(String str)
    {
        this.name = str;
    }

    public String getString(String message)
    {
        return StringUtil.Color(this.name) + message;
    }

    @Override
    public String toString()
    {
        try
        {
            return this.name + "@" + IntegrityChecker.sha1(this.name);
        }
        catch(NoSuchAlgorithmException e)
        {
            return null;
        }
    }
}
