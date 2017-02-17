package io.github.kunonx.DesignFramework.adapter;

import io.github.kunonx.DesignFramework.Permission;
import io.github.kunonx.DesignFramework.gson.*;

import java.lang.reflect.Type;

public class PermissionAdapter implements JsonSerializer<Permission>, JsonDeserializer<Permission>
{
    @Override
    public Permission deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject jsonObject = (JsonObject)json;
        Permission permission = new Permission(jsonObject.get("permission").getAsString(), jsonObject.get("default").getAsBoolean());
        return permission;
    }

    @Override
    public JsonElement serialize(Permission src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject json = new JsonObject();
        json.addProperty("permission", src.getPermission());
        json.addProperty("default", src.isDefaultOp());
        return json;
    }
}
