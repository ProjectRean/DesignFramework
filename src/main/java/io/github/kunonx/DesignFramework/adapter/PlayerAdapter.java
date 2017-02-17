package io.github.kunonx.DesignFramework.adapter;

import io.github.kunonx.DesignFramework.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Created by GIGABYTE on 2017-02-16.
 */
public class PlayerAdapter implements JsonSerializer<Player>, JsonDeserializer<Player>
{
    @Override
    public JsonElement serialize(Player src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject json = new JsonObject();
        String id = src.getUniqueId().toString();
        json.addProperty("playerId", id);
        return json;
    }

    @Override
    public Player deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject object = (JsonObject)json;
        return Bukkit.getPlayer(UUID.fromString(object.get("playerId").getAsString()));
    }
}
