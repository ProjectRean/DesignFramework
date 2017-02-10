package io.github.kunonx.DesignFramework;

public interface Type<C>
{
    <C> Class<C> getGenericType();
}
