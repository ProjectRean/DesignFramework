package io.github.kunonx.DesignFramework.test;

import io.github.kunonx.DesignFramework.gson.Gson;
import io.github.kunonx.DesignFramework.gson.GsonBuilder;
import io.github.kunonx.DesignFramework.message.Prefix;

import java.util.UUID;

public class Entity<E> implements EntitySerialization<E>
{
    public String id = UUID.randomUUID().toString();
    public Prefix prefix = new Prefix("sfaf");

    @Override
    public E get(Object obj) {
        return null;
    }

    @Override
    public E getEntity(Object obj, boolean create) {
        return null;
    }

    public Entity<E> create(String json, Class<Entity<E>> clazz)
    {
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        return g.fromJson(json, clazz);
    }

    public Entity()
    {}


    public Entity(String id, Class<E> clazz)
    {
        if(id == null) id = UUID.randomUUID().toString();

    }

    @Override
    public String serialize()
    {
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        new GsonBuilder().registerTypeAdapter(Entity.class, new EntityElementAdapter());
        String jsonString = g.toJson(this);
        return jsonString;
    }

}
