package io.github.kunonx.DesignFramework.collection;

import java.util.Map.Entry;

public class StringEntry implements Entry<String, String>
{
    public StringEntry(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    private String key;

    private String value;

    public String setKey(String key)
    {
        this.key = key;
        return this.key;
    }

    @Override
    public String getKey()
    {
        return this.key;
    }

    @Override
    public String getValue()
    {
        return this.value;
    }

    @Override
    public String setValue(String value)
    {
        this.value = value;
        return this.value;
    }
}
