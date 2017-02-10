package io.github.kunonx.DesignFramework.core;

import io.github.kunonx.DesignFramework.entity.world.WorldLocation;

/**
 * Created by GIGABYTE on 2017-02-04.
 */
public class WorldLocationCore extends Core
{
    public static WorldLocationCore instance = new WorldLocationCore();
    public static WorldLocationCore getInstance() { return instance; }

    @Override
    public void run() {
        WorldLocation.load();
    }
}
