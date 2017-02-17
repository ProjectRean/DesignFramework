package io.github.kunonx.DesignFramework;

import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

public class BossBarTimer extends BukkitRunnable
{
    private BossBar b;
    private double progress;

    public BossBarTimer(BossBar bar, int ticks, double progress)
    {
        this.b = bar;
        this.progress = (progress / ticks);
    }

    public void run()
    {
        double prog = this.b.getProgress() - this.progress;
        if (prog < 0.0D)
        {
            this.b.setVisible(false);
            cancel();
        }
        else
        {
            this.b.setProgress(prog);
        }
    }
}

