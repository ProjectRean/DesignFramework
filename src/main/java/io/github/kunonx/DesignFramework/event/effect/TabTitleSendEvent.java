package io.github.kunonx.DesignFramework.event.effect;

import io.github.kunonx.DesignFramework.event.DesignFrameworkEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class TabTitleSendEvent extends DesignFrameworkEvent
{
    private final Player player;
    private String header;
    private String footer;

    public TabTitleSendEvent(Player player, String header, String footer)
    {
        this.player = player;
        this.header = header;
        this.footer = footer;
    }

    public Player getPlayer()
    {
        return player;
    }

    public String getHeader()
    {
        return header;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public String getFooter()
    {
        return footer;
    }

    public void setFooter(String footer)
    {
        this.footer = footer;
    }

    private static final HandlerList Handler = new HandlerList();

    @Override public HandlerList getHandlers() { return Handler; }

    public HandlerList getHandlerList() { return getHandlers(); }
}