package io.github.kunonx.DesignFramework.core;

import io.github.kunonx.DesignFramework.SyncConfigFileReader;

public class ConfigurationCore extends Core
{
    private static ConfigurationCore instance = new ConfigurationCore();
    public static ConfigurationCore getInstance() { return instance; }

    @Override
    public void run()
    {
        for(Object o : SyncConfigFileReader.getRegister())
        {
            if(o instanceof SyncConfigFileReader)
            {
                ((SyncConfigFileReader) o).refresh();
            }
        }
    }
}
