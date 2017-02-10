package io.github.kunonx.DesignFramework.system;

import io.github.kunonx.DesignFramework.collection.StringEntry;
import io.github.kunonx.DesignFramework.message.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * VariableString is an enumeration class created to replace substitution values in strings.<br>
 * DesignFrameworkPlugin creates a permutation enumeration to use in advance.<br>
 *<br>
 * Use DesignFrameWorkPlugin to create a pre-used replacement column and save the value of the string that points to it.<br>
 * <br>
 * <b>Key</b> : The string that points to an enumeration<br>
 * <b>Value</b> : The string pointing to the enum in the replacement format<br>
 *
 * @version 1.0
 * @author kunonx
 */
public class VariableString implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**
     * Store values and manage variable values.
     * <b>Key</b> : The string that points to an enumeration<br>
     * <b>Value</b> : The string pointing to the enum in the replacement format<br>
     */
    private static final transient Map<String, String> defined_map = new HashMap<String, String>();
    public static Map<String, String> defines() { return defined_map; }

    private static final String format = "<%s>";
    public static String getFormat() { return format; }

    static
    {
        __defined("USUGE_WORLD", "usuge_world");
        __defined("TIME", "time");
        __defined("MAX_PLAYER", "max_player");
        __defined("USING_PLAYER", "usuge_player");
        __defined("PLAYER_NAME", "player");
        __defined("MONEY", "money");
        __defined("WEATHER", "weather");
        __defined("WORLD", "world");
        __defined("AUTO", "auto");
        __defined("ONLINE_PLAYER", "online_player");
        __defined("DEFAULT", "default");
    }

    protected static void __defined(String name, String v)
    {
        defined_map.put(name, v);
    }

    public static String getSymbolFormatted(String str)
    {
        return String.format(VariableString.getFormat(), str);
    }

    /**
     * Determines whether one or more enumerated types in the string indicate the replacement type. It supports strings of the FormatString type.
     * @param str A random replacement string that points to an enumerated type
     * @return Returns true if it is a string represented by a StringValue, otherwise false.
     */
    public static boolean isDefined(String str)
    {
        if((StringUtil.isSymbolFormatted(VariableString.getFormat(), str)))
            str = StringUtil.removeSymbolFormat(VariableString.getFormat(), str);
        for(String s : VariableString.defines().keySet())
        {
            if(s.equalsIgnoreCase(str)) return true;
        }
        return false;
    }


    /**
     * Determines if the replacement value defined by the plugin contains at least one of the strings.
     * @param str A string containing the replacement value
     * @return Returns <code> true </code> if the string contains a replacement value defined within the plugin, otherwise <code>false</code>.
     * @see VariableString#defined_map
     * @version 1.0.0
     * @author SkaiDream
     */
    public static boolean containsValue(String str)
    {
        for(String s2 : VariableString.defines().values())
        {
            s2 = String.format(VariableString.getFormat(), s2);
            if(s2.equalsIgnoreCase(str)) return true;
        }
        return false;
    }

    /**
     * Find all StringValues that represent this using the substitution values contained in the string received from the parameter.
     * @param key A string containing the replacement value
     * @see VariableString#defined_map
     * @return Returns the StringValue contained in the string as a Collection in the parameter
     * @author SkaiDream
     */
    public static Entry<String, String> getVariableString(String key)
    {
        for(String s : VariableString.defines().keySet())
        {
            if(s.equalsIgnoreCase(key))
                return new StringEntry(s, VariableString.getValueFormatted(s));
        }
        return null;
    }

    public static String getValueFormatted(String key)
    {
        for(String s : VariableString.defines().keySet())
        {
            if(s.equalsIgnoreCase(key))
                return getSymbolFormatted(VariableString.defines().get(s));
        }
        return null;
    }

    /**
     * Determines if the string representing <code>StringValue</code> is included in the string received from the parameter and returns the corresponding that.
     * @param str String containing the replacement value
     * @param key <Code>StringValue</code> with a string pointing to the enumeration
     * @see VariableString#defined_map
     * @version 1.0.0
     * @author SkaiDream
     **/
    public static boolean contains(String str, String key)
    {
        if(str.contains(getSymbolFormatted(defines().get(key)))) return true;
        return false;
    }

    /**
     * Reads the replacement value contained in the string from the Object type and replaces it. If the Object type for the substitution value is not correct, ignoring it.
     * @param s A string containing the replacement value
     * @param required Objects to replace the actual value of the replacement value
     * @return Returns string with replacement changed value
     */
    public static String replaceAll(String s, Object... required)
    {
        if(VariableString.containsValue(s))
        {
            s = __define_relating_bukkit_replacement(s);
            if(!((required.length == 0) || (required == null)))
                for(Object o : required)
                {
                    if(o instanceof Player)
                        s = __define_relating_player_replacement(s, (Player)o);
                    else if(o instanceof World)
                        s = __define_relating_world_replacement(s, (World)o);
                }
        }
        return s;
    }

    private static String __define_relating_bukkit_replacement(String str)
    {
        if(VariableString.contains(str, "ONLINE_PLAYER"))
            str = str.replaceAll(getValueFormatted("ONLINE_PLAYER"), String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));
        if(VariableString.contains(str, "MAX_PLAYER"))
            str = str.replaceAll(getValueFormatted("MAX_PLAYER"), String.valueOf(Bukkit.getServer().getMaxPlayers()));
        //TODO
        return str;
    }

    private static String __define_relating_player_replacement(String str, Player player)
    {
        if(VariableString.contains(str, "PLAYER_NAME"))
            str = str.replaceAll(getValueFormatted("PLAYER_NAME"), player.getName());
        //TODO
        return str;
    }

    private static String __define_relating_world_replacement(String str, World world)
    {
        if(VariableString.contains(str, "WORLD"))
            str = str.replaceAll(getValueFormatted("WORLD"), world.getName());
        if(VariableString.contains(str, "WEATHER"))
            str = str.replaceAll(getValueFormatted("WEATHER"), world.hasStorm() ? "STORM" : "SUNNY");
        if(VariableString.contains(str, "TIME"))
            str = str.replaceAll(getValueFormatted("TIME"), String.valueOf(world.getTime()));
        if(VariableString.contains(str, "USING_PLAYER"))
            str = str.replaceAll(getValueFormatted("USING_PLAYER"), String.valueOf(world.getPlayers().size()));
        if(VariableString.contains(str, "USUGE"))
        {
            double f = world.getPlayers().size() / (double)Bukkit.getServer().getOnlinePlayers().size() * 100.0;
            str = str.replaceAll(getValueFormatted("USUGE"), String.valueOf(f));
        }
        //TODO
        return str;
    }
}
