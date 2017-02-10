package io.github.kunonx.DesignFramework.message;

import io.github.kunonx.DesignFramework.plugin.DesignFrameworkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.io.File;

public final class Msg
{
    public enum MsgLevel
    {
        ERROR(0, "&4"),
        DANGER(1, "&c"),
        WARNING(2, "&e"),
        COMMON(3, "&f"),
        MESSAGE(4, "&a"),
        UNKNOWN(5, "&8");

        private int code;
        private String color;

        MsgLevel(int code, String color)
        {
            this.code = code;
            this.color = color;
        }

        public int getCode()
        {
            return this.code;
        }

        public String getColor()
        {
            return this.color;
        }
    }

    @Nullable private String prefix = null;
    public String getPrefix() { return this.prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }

    @Nullable  private String suffix = null;
    public String getSuffix() {  return this.suffix; }
    public void setSuffix(String suffix) { this.suffix = suffix; }

    public Msg(String prefix)
    {
        this.prefix = StringUtil.Color(prefix);
    }
    public Msg(Prefix prefix)
    {
        this.prefix = prefix.getName();
    }
    public Msg(String prefix, String suffix)  { this.prefix = prefix; this.suffix = suffix; }

    public void send(CommandSender sender, String message)  { this.send(sender, message, null); }
    public void send(CommandSender sender, String message, Object... values)
    {
        if(this.prefix != null) message = prefix + message;
        if(this.suffix != null) message = message + suffix;
        message = StringUtil.replaceValue(message, values);
        sender.sendMessage(StringUtil.Color(message));
    }

    public void sendToConsole(String message)  { this.sendToConsole(message, null); }
    public void sendToConsole(String message, Object... values)
    {
        if(this.prefix != null) message = prefix + message;
        if(this.suffix != null) message = message + suffix;
        Bukkit.getConsoleSender().sendMessage(StringUtil.Color(StringUtil.replaceValue(message, values)));
    }

    public static void sendTxt(CommandSender sender, String message, Object... values)  { sender.sendMessage(StringUtil.Color(StringUtil.replaceValue(message, values))); }
    public static void sendTxt(DesignFrameworkPlugin plugin, CommandSender sender, String message, Object... values)
    {
        if(plugin != null) message = plugin.getPrefix().getName() + message;
        sender.sendMessage(StringUtil.Color(StringUtil.replaceValue(message, values)));
    }

    public static void log(DesignFrameworkPlugin plugin, String message)
    {
        Msg.log(plugin, MsgLevel.MESSAGE, message);
    }
    public static void log(DesignFrameworkPlugin plugin, MsgLevel level, String message)
    {
        File file = new File(plugin.getDataFolder().getParentFile() + "/DesignFramework_Log/" + ".log");
    }

    public static void console(DesignFrameworkPlugin plugin, String message)  { console(plugin, message, null); }
    public static void console(DesignFrameworkPlugin plugin, String message, Object... values)
    {
        plugin.getPluginMsg().sendToConsole(message, values);
    }

}
