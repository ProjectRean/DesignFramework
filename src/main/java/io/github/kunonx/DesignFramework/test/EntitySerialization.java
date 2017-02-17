package io.github.kunonx.DesignFramework.test;


public interface EntitySerialization<E>
{
    E get(Object obj);

    E getEntity(Object obj, boolean create);

    String serialize();


}
