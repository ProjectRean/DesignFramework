package io.github.kunonx.DesignFramework.test;

import io.github.kunonx.DesignFramework.gson.JsonElement;
import io.github.kunonx.DesignFramework.gson.JsonObject;
import io.github.kunonx.DesignFramework.gson.JsonParseException;
import io.github.kunonx.DesignFramework.gson.JsonSerializer;
import io.github.kunonx.DesignFramework.gson.JsonDeserializer;
import io.github.kunonx.DesignFramework.gson.JsonDeserializationContext;
import io.github.kunonx.DesignFramework.gson.JsonPrimitive;
import io.github.kunonx.DesignFramework.gson.JsonSerializationContext;

import java.lang.reflect.Type;

public class EntityElementAdapter implements JsonSerializer<Entity>, JsonDeserializer<Entity>
{
    @Override
    public JsonElement serialize(Entity src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        result.add("properties", context.serialize(src, src.getClass()));

        return result;
    }

    @Override
    public Entity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        try
        {
            return context.deserialize(element, Class.forName("io.github.kunonx.DesignFramework.test." + type));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }
}