package io.github.kunonx.DesignFramework;

import io.github.kunonx.DesignFramework.core.CommandRegisterationCore;
import io.github.kunonx.DesignFramework.core.ConfigurationCore;
import io.github.kunonx.DesignFramework.core.WorldLocationCore;
import io.github.kunonx.DesignFramework.entity.world.WorldLocation;
import io.github.kunonx.DesignFramework.nms.EnchantmentTag;
import io.github.kunonx.DesignFramework.plugin.DesignFrameworkPlugin;

public class DesignFramework extends DesignFrameworkPlugin
{
    public static DesignFramework instance;
    public static DesignFramework getInstance() { return instance; }

    @Override
    protected void onEnableInner()
    {
        instance = this;
        this.startActivation(CommandRegisterationCore.class, ConfigurationCore.class, WorldLocationCore.class);
        WorldLocation.load();
        EnchantmentTag.setup();
    }

    @Override
    protected void onDisableInner()
    {
        //TODO
    }
}
