package io.github.kunonx.DesignFramework.message;

import io.github.kunonx.DesignFramework.BossBarTimer;
import io.github.kunonx.DesignFramework.DesignFramework;
import io.github.kunonx.DesignFramework.plugin.DesignFrameworkPlugin;

import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;
import net.minecraft.server.v1_11_R1.PacketPlayOutTitle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

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

    private String prefix = null;
    public String getPrefix() { return this.prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }

    private String suffix = null;
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

    public static void sendActionMessage(Player p, String message)
    {
        IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \""+ ChatColor.translateAlternateColorCodes('&', message)+"\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(bar);
    }
    public static void sendBossBarMessage(Player player, BarColor color, BarStyle style, String message, double process, int time)
    {
        BossBar b = Bukkit.createBossBar(message, color, style, new BarFlag[0]);
        b.addPlayer(player);
        BossBarTimer s = new BossBarTimer(b, time, process);
        s.runTaskTimer(DesignFramework.getInstance(), 5L, 1L);
    }

    public static void sendTitleMessage(Player p, int fadein, int time, int fadeout, String main, String sub)
    {
        title(main, p, fadein, time, fadeout);
        subTitle(sub, p, fadein, time, fadeout);
    }

    public static void title(String message, Player p, int fadein, int time, int fadeout)
    {
        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"), fadein, time, fadeout);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(title);
    }

    public static void subTitle(String message, Player p, int fadein, int time, int fadeout)
    {
        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"), fadein, time, fadeout);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(subtitle);
    }
}
