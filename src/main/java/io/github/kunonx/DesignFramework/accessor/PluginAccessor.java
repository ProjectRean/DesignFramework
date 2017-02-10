package io.github.kunonx.DesignFramework.accessor;


public interface PluginAccessor
{
    void reload();

    void restart();

    boolean isDisabled();

    boolean isNeverEnabled();

    void startActivation(Object... objects);

    void stopActivation(Object... objects);
}
