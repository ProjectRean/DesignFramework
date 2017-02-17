package io.github.kunonx.DesignFramework.event.effect;

import io.github.kunonx.DesignFramework.event.DesignFrameworkEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class TitleSendEvent extends DesignFrameworkEvent
{
    private final Player player;
    private String title;
    private String subtitle;

    public TitleSendEvent(Player player, String title, String subtitle)
    {
        this.player = player;
        this.title = title;
        this.subtitle = subtitle;
    }
    
    public Player getPlayer()
    {
        return player;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getSubtitle()
    {
        return subtitle;
    }

    public void setSubtitle(String subtitle)
    {
        this.subtitle = subtitle;
    }

    private static final HandlerList Handler = new HandlerList();

    @Override public HandlerList getHandlers() { return Handler; }

    public HandlerList getHandlerList() { return getHandlers(); }
}