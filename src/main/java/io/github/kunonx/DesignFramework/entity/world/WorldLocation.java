package io.github.kunonx.DesignFramework.entity.world;

import io.github.kunonx.DesignFramework.DesignFramework;
import io.github.kunonx.DesignFramework.accessor.StaticActivator;
import io.github.kunonx.DesignFramework.entity.AbstractLocation;
import io.github.kunonx.DesignFramework.plugin.config.SyncYamlConfiguration;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorldLocation extends AbstractLocation implements StaticActivator
{
    protected static SyncYamlConfiguration syncYaml = SyncYamlConfiguration.initLoad
            (DesignFramework.getInstance(), new File(DesignFramework.getInstance().getDataFolder(), "data/DesignFramework/WorldLocation.yml"));

    protected static final transient List<WorldLocation> named_instance = new ArrayList<WorldLocation>();
    public static List<WorldLocation> getRegister() { return named_instance; }
    public static boolean isRegistered(String name)
    {
        for(WorldLocation wl : named_instance)
        {
            if(wl.name.equalsIgnoreCase(name))
            {
                return true;
            }
        }
        return false;
    }

    public WorldLocation(String name, Location location)
    {
        super(name, null, location);
    }

    public WorldLocation(String name, String id, Location location)
    {
        super(name, id, location);
    }

    @Override
    public void setName(String name)
    {
        if(name == null) throw new IllegalStateException("name cannot be null");

        // Remove corresponding name
        this.remove(name);

        // refresh database
        this.name = name;
        this.save();
    }

    public void remove()
    {
        // Remove corresponding name
        syncYaml.removeKey(this.name);
        syncYaml.save();
    }

    public void remove(String name)
    {
        // Remove corresponding name
        syncYaml.removeKey(name);
        syncYaml.save();
    }

    @Override
    public void setActive(boolean enable)
    {
        if(enable)
        {
            if(! this.isActive())
            {
                named_instance.add(this);
            }
        }
        else
        {
            named_instance.remove(this);
        }
    }

    @Override
    public boolean isActive()
    {
        for(WorldLocation l : named_instance)
        {
            if(l.equals(this))
                return true;
        }
        return false;
    }

    public void teleport(Player player) { player.teleport(this.getLocation()); }

    private static transient List<String> __exec_handling_name = new ArrayList<String>();

    public void save()
    {
        YamlConfiguration y = syncYaml.getYaml();
        y.set(this.getName() + ".Location.World", this.getWorld().getName());
        y.set(this.getName() + ".Location.X", this.getLocation().getX());
        y.set(this.getName() + ".Location.Y", this.getLocation().getY());
        y.set(this.getName() + ".Location.Z", this.getLocation().getZ());
        y.set(this.getName() + ".Location.Pitch", this.getLocation().getPitch());
        y.set(this.getName() + ".Location.Yaw", this.getLocation().getYaw());
        syncYaml.save();
    }

    private static void reloadDatabase()
    {
        named_instance.clear();
        YamlConfiguration y = syncYaml.getYaml();
        for(String name : y.getKeys(false))
        {
            if(__exec_handling_name.contains(name)) continue;
            try
            {
                WorldLocation w = WorldLocation.create(name, syncYaml.getString(name, "ID"), new Location(
                                Bukkit.getServer().getWorld(syncYaml.getString(name, "Location", "World")),
                                syncYaml.getDouble(name, "Location", "X"),
                                syncYaml.getDouble(name, "Location", "Y"),
                                syncYaml.getDouble(name, "Location", "Z"),
                                syncYaml.getFloat(name, "Location", "Pitch"),
                                syncYaml.getFloat(name, "Location", "Yaw")));
                w.setActive(DesignFramework.getInstance() != null);
            }
            catch(NullPointerException e)
            {
                if(! __exec_handling_name.contains(name))
                {
                    __exec_handling_name.add(name);
                }
                else
                {
                    DesignFramework.getInstance().getPluginMsg().sendToConsole("Exception handling occurred. The key {0} is unknown.", name);
                }
                continue;
            }
        }
    }

    public static void load()
    {
        if(! named_instance.isEmpty())
        {
            WorldLocation.reloadDatabase();
            return;
        }
        YamlConfiguration y = syncYaml.getYaml();
        for(String name : y.getKeys(false))
        {
            if(__exec_handling_name.contains(name)) continue;
            try
            {
                WorldLocation w = new WorldLocation(name, syncYaml.getString(name, "ID"), new Location(
                                Bukkit.getServer().getWorld(syncYaml.getString(name, "Location", "World")),
                                syncYaml.getDouble(name, "Location", "X"),
                                syncYaml.getDouble(name, "Location", "Y"),
                                syncYaml.getDouble(name, "Location", "Z"),
                                syncYaml.getFloat(name, "Location", "Pitch"),
                                syncYaml.getFloat(name, "Location", "Yaw")));

                w.setActive(DesignFramework.getInstance() != null);
            }
            catch(IllegalArgumentException e)
            {
                if(! __exec_handling_name.contains(name))
                {
                    e.printStackTrace();
                    __exec_handling_name.add(name);
                    DesignFramework.getInstance().getPluginMsg().sendToConsole("&cException handling occurred from WorldLocation. The key \"{0}\" have wrong database or syntax incorrect. skipping", name);
                }
                continue;
            }
            catch(NullPointerException e)
            {
                if(! __exec_handling_name.contains(name))
                {
                    e.printStackTrace();
                    __exec_handling_name.add(name);
                    DesignFramework.getInstance().getPluginMsg().sendToConsole("&cException handling occurred from WorldLocation. The key \"{0}\" have wrong database or syntax incorrect. skipping", name);
                }
                continue;
            }
        }
    }

    public static boolean hasDefault(World world)
    {
        YamlConfiguration y = syncYaml.getYaml();
        for(String name : y.getKeys(false))
        {
            if(y.getString(name + ".Location.World").equalsIgnoreCase(world.getName()))
            {
                if (y.getBoolean(name + ".Default"))
                {
                    return true;
                }
                else
                {
                    continue;
                }
            }
        }
        return false;
    }

    public List<WorldLocation> getWorldLocation(World filter)
    {
        YamlConfiguration y = syncYaml.getYaml();
        List<WorldLocation> selected = new ArrayList<WorldLocation>();
        for(String name : y.getKeys(false))
        {
            if(y.getString(name + ".Location.World").equalsIgnoreCase(filter.getName()))
            {
                selected.add(WorldLocation.get(name));
            }
        }
        return selected;
    }

    public List<String> getWorldLocationName(World filter)
    {
        YamlConfiguration y = syncYaml.getYaml();
        List<String> selected = new ArrayList<String>();
        for(String name : y.getKeys(false))
        {
            if(y.getString(name + ".Location.World").equalsIgnoreCase(filter.getName()))
            {
                selected.add(name);
            }
        }
        return selected;
    }

    public static WorldLocation get(String name)
    {
        if(isRegistered(name))
        {
            for(WorldLocation w : named_instance)
            {
                if(w.getName().equalsIgnoreCase(name))
                {
                    return w;
                }
            }
        }
        return null;
    }

    public static WorldLocation create(String name, Location location)
    {
        return create(name, null, location);
    }

    public static WorldLocation create(String name, String id, Location location)
    {
        if(isRegistered(name))
        {
            for (WorldLocation w : named_instance)
            {
                if (w.getName().equalsIgnoreCase(name))
                {
                    w.setLocation(location);
                    w.setName(name);
                    return w;
                }
            }

        }
        else
        {
            if(id == null) id = UUID.randomUUID().toString();
            YamlConfiguration y = syncYaml.getYaml();
            boolean def = WorldLocation.hasDefault(location.getWorld());
            y.set(name + ".Default", def);
            y.set(name + ".ID", id);
            y.set(name + ".Location.World", location.getWorld().getName());
            y.set(name + ".Location.X", location.getX());
            y.set(name + ".Location.Y", location.getY());
            y.set(name + ".Location.Z", location.getZ());
            y.set(name + ".Location.Pitch", location.getPitch());
            y.set(name + ".Location.Yaw", location.getYaw());
            syncYaml.save();
        }
        return WorldLocation.get(name);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null) return false;
        if(obj instanceof WorldLocation)
        {
            return ((WorldLocation) obj).getName().equals(this.getName()) && ((WorldLocation) obj).getLocation() == this.getLocation();
        }
        else
        {
            return false;
        }
    }
}
