package io.github.kunonx.DesignFramework.entity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.Serializable;
import java.util.UUID;

public abstract class AbstractLocation implements Serializable
{
    private static transient final long serialVersionUID = 1L;

    protected AbstractLocation(String name, Location location)
    {
        this(name, null, location);
    }

    protected AbstractLocation(String name, String id, Location location)
    {
        try
        {
            if(id == null) id = UUID.randomUUID().toString();
            else
            {
                UUID.fromString(id);
            }
        }
        catch(IllegalArgumentException e)
        {
            id = UUID.randomUUID().toString();
        }
        this.id = id;
        this.name = name;
        this.world = location.getWorld();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    private World world;
    public World getWorld() { return this.world; }
    public void setWorld(World world) { this.world = world; }
    public void setWorld(String world) { this.world = Bukkit.getServer().getWorld(world); }

    private double x;
    public double getX() { return this.x; }
    public void setX(double x) { this.x = x; }

    private double y;
    public double getY() { return this.y; }
    public void setY(double y) { this.y = y; }

    private double z;
    public double getZ() { return this.z; }
    public void setZ(double z) { this.z = z; }

    private float yaw;
    public float getYaw() { return this.yaw; }
    public void setYaw(float yaw) { this.yaw = yaw; }

    private float pitch;
    public float getPitch() { return this.pitch; }
    public void setPitch(float pitch) { this.pitch = pitch; }

    public Location getLocation() { return new Location(this.world, this.x, this.y, this.z, this.yaw, this.pitch); }

    public void setLocation(Location loc)
    {
        this.world = loc.getWorld();
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
        this.yaw = loc.getYaw();
        this.pitch = loc.getPitch();
    }

    protected String name;
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    protected final String id;
    public String getId() { return this.id; }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null) return false;
        if(obj instanceof String)
        {
            return UUID.fromString((String)obj).toString().equals(this.getId());
        }
        else if(obj instanceof AbstractLocation)
        {
            AbstractLocation location = (AbstractLocation)obj;
            return location.getLocation() == this.getLocation() && location.getId().equals(this.getId());
        }
        else
        {
            return false;
        }
    }
}
