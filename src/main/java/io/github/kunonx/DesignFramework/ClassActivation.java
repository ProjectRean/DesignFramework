package io.github.kunonx.DesignFramework;

import io.github.kunonx.DesignFramework.plugin.DesignFrameworkPlugin;

public interface ClassActivation
{
    void setEnabled(boolean enable);

    void setEnabled(DesignFrameworkPlugin plugin);

    boolean isEnabled();
}
