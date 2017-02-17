package io.github.kunonx.DesignFramework.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import io.github.kunonx.DesignFramework.DesignFramework;
import io.github.kunonx.DesignFramework.command.CustomizeBukkitCommand;
import io.github.kunonx.DesignFramework.command.CustomizeCommand;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;


/**
 * CommandRegisterationCore is a class that registers a custom command as the basic command of the Bukkit server.
 * This is handled automatically by Core even if you don't list the commands in the plugin.yml.
 * @author SkaiDream
 * @version 1.0.1
 * @since 1.0.0
 */
public class CommandRegisterationCore extends Core
{
    public static CommandRegisterationCore instance = new CommandRegisterationCore();
    public static CommandRegisterationCore getInstance() { return instance; }

    @Override
    public void run()
    {
        registerCommand();
    }

    private static void registerCommand()
    {
        SimpleCommandMap commandMap = getSimpleCommandMap();
        Map<String, Command> knownCommands = getSimpleCommandMapRegistered(commandMap);
        Map<String, CustomizeCommand<?>> nameTargets = new HashMap<String, CustomizeCommand<?>>();
        for (CustomizeCommand<?> abstractCommand : CustomizeCommand.getRegisterCommands())
        {
            for (String alias : abstractCommand.getAliases())
            {
                if (alias == null) continue;

                alias = alias.trim().toLowerCase();
                nameTargets.put(alias, abstractCommand);
            }
        }

        for (Entry<String, CustomizeCommand<?>> entry : nameTargets.entrySet())
        {
            String name = entry.getKey();
            CustomizeCommand<?> target = entry.getValue();
            target.setEnabled(DesignFramework.getInstance());

            Command current = knownCommands.get(name);
            CustomizeCommand<?> commandTarget = getCustomizeCommand(current);

            if (target == commandTarget) continue;

            if (current != null)
            {
                knownCommands.remove(name);
                current.unregister(commandMap);
            }

            CustomizeBukkitCommand command = new CustomizeBukkitCommand(name, target);

            Plugin plugin = command.getCustomizeCommand().getActivePlugin();
            String pluginName = (plugin != null ? plugin.getName() : "DesignFramework");
            commandMap.register(pluginName, command);
        }
    }

    public static CustomizeCommand<?> getCustomizeCommand(Command command)
    {
        if (command == null) return null;
        if (!(command instanceof CustomizeBukkitCommand)) return null;
        CustomizeBukkitCommand cbc = (CustomizeBukkitCommand)command;
        return cbc.getCustomizeCommand();
    }

    protected static Field commandMapField = getField(Bukkit.getServer().getClass(), "commandMap");
    protected static Field simpleCommandField = getField(SimpleCommandMap.class, "knownCommands");

    public static SimpleCommandMap getSimpleCommandMap()
    {
        Server server = Bukkit.getServer();
        return getField(commandMapField, server);
    }
    public static Map<String, Command> getSimpleCommandMapRegistered(SimpleCommandMap simpleCommandMap)
    {
        return getField(simpleCommandField, simpleCommandMap);
    }

    public static Field getField(Class<?> clazz, String name)
    {
        try
        {
            Field ret = clazz.getDeclaredField(name);
            ret.setAccessible(true);
            return ret;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getField(Field field, Object object)
    {
        try
        {
            return (T) field.get(object);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
