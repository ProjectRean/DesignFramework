package io.github.kunonx.DesignFramework.command;

import io.github.kunonx.DesignFramework.ClassActivation;
import io.github.kunonx.DesignFramework.Parameter;
import io.github.kunonx.DesignFramework.Permission;
import io.github.kunonx.DesignFramework.Type;
import io.github.kunonx.DesignFramework.json.message.FancyMessage;
import io.github.kunonx.DesignFramework.message.Msg;
import io.github.kunonx.DesignFramework.message.Prefix;
import io.github.kunonx.DesignFramework.message.StringUtil;
import io.github.kunonx.DesignFramework.message.Defined;
import io.github.kunonx.DesignFramework.plugin.DesignFrameworkPlugin;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * CustomizeCommand is an abstraction command skeleton that allows you to register a command directly to Bukkit without any setting.
 * This is an extended of original function, which allows the developer to easily skip the complex process of registering commands in the game and do it easily.
 * It uses a self-referencing generic to avoid errors in grammar settings. The developer can determine whether the class is activated by ClassActivation.
 * It cannot be used even if the command is registered with Bukkit when It's deactivated.<br><br>
 *
 * <b>About self-referencing generic class</b>
 * http://www.angelikalanger.com/GenericsFAQ/FAQSections/ProgrammingIdioms.html#FAQ206
 *
 * @author Kunonx
 * @since 1.0.0
 * @version 1.2.0
 * @param <C> The class type that inherits from CustomizeCommand, Preventing Inheritance error
 */
public abstract class CustomizeCommand<C extends CustomizeCommand<C>> implements ClassActivation, CommandExecutable, Type<C>
{
    ///////////////////////////////
    //         INSTANCE          //
    ///////////////////////////////
    private static transient final List<CustomizeCommand<?>> registerCommands = new ArrayList<CustomizeCommand<?>>();
    public static List<CustomizeCommand<?>> getRegisterCommands() { return registerCommands; }

    /**
     * The generic class for LattCommand. This is the prototype of the class of the superclass.
     */
    private final Class<C> genericClazz;
    @SuppressWarnings("unchecked")
    @Override public Class<C> getGenericType() { return this.genericClazz; }

    public C getGenericInstance()
    {
        try
        {
            C generic = this.genericClazz.newInstance();
            return generic;
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
            return null;
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a constructor for the LattCommand and holds information about the generic class. No other features are currently implemented.
     */
    @SuppressWarnings("unchecked")
    public CustomizeCommand()
    {
        this.genericClazz = (Class<C>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    ///////////////////////////////
    //        ACTIVATION         //
    ///////////////////////////////
    protected DesignFrameworkPlugin activePlugin;
    public DesignFrameworkPlugin getActivePlugin() { return this.activePlugin; }


    @Override
    public void setEnabled(DesignFrameworkPlugin plugin)
    {
        this.activePlugin = plugin;
        this.setEnabled(plugin != null);
    }

    @Override
    public void setEnabled(boolean active)
    {
        if(active)
        {
            if(this.isEnabled()) return;
            registerCommands.add(this);
        }
        else
        {
            if(this.isEnabled()) { registerCommands.remove(this); }
        }
    }

    @Override public boolean isEnabled() { return registerCommands.contains(this); }

    ///////////////////////////////
    //           MAIN            //
    ///////////////////////////////
    protected Prefix prefix                                  = null;
    public boolean hasPrefix()                               { return this.prefix != null; }
    public Prefix getPrefix()                                { return this.prefix; }
    public void setPrefix(Prefix prefix)                     { this.prefix = prefix; }

    private Permission permission                            = null;
    public void setPermission(Permission permission)         { this.permission = permission; }
    public void setPermission(String permission)             { this.permission = new Permission(permission, true); }
    public String getPermission()                            { return permission.getPermission(); }
    public String getPermissionWithChild(String child)       { return this.permission.getPermission() + "." + child; }
    public Permission getPermissionType()                    { return this.permission; }
    public String getPermissionMessage()                     { return this.permission.getMessage(); }
    public boolean hasPermission()                           { return this.permission != null; }
    public boolean hasPermission(Player player)
    {
        if(player.isOp()) return true;
        if(! this.hasPermission()) return true;
        return player.hasPermission(this.permission.getPermission());
    }

    public boolean hasChildPermission(Player player, String perm)
    {
        if(! perm.startsWith("."))
        {
            perm = "." + perm;
        }
        return player.hasPermission(this.getPermission() + perm);
    }

    protected List<String> detailDesc                        = new ArrayList<String>();
    public boolean hasDetailDescription()                    { return ! this.detailDesc.isEmpty(); }
    public void addDetailDescription(String desc)            { this.detailDesc.add(StringUtil.Color(desc)); }
    public void addDetailDescription(List<String> desc)      { this.detailDesc.addAll(StringUtil.ColorStringList(desc)); }
    public void setDetailDescription(String desc, int index) { this.detailDesc.set(index, desc); }
    public void clearDetailDescription()                     { this.detailDesc.clear(); }
    public void removeDetailDescription(int index)           { this.detailDesc.remove(index); }
    public List<String> getDetailDescription()               { return this.detailDesc; }

    private List<Parameter> parameter                        = new ArrayList<Parameter>();
    public List<Parameter> getParameter()                    { return this.parameter; }
    public List<Parameter> getAvailableParameter(CommandSender sender)
    {
        //TODO
        return null;
    }
    public void setParameter(List<Parameter> param)          { this.parameter = param; }
    public void addParameter(Parameter param)                { this.parameter.add(param); }
    public boolean hasParameter()                            { return !this.parameter.isEmpty(); }
    public boolean hasRequirementParam()                     { if(this.parameter.isEmpty()) return false; return this.parameter.get(0).isRequirement(); }

    public String getMainCommand()                           { return this.aliases.get(0); }
    public PluginCommand getPluginCommand()                  { return this.getActivePlugin().getCommand(this.getMainCommand()); }
    protected String description                             = Defined.NO_DESC;
    public String getDescription()                           { return this.description; }
    public void setDescription(String desc)                  { this.description = desc; }

    private List<CustomizeCommand<?>> mainExternalCommand    = new ArrayList<CustomizeCommand<?>>();
    public void addMainExternalCommand(CustomizeCommand<?> command){ this.mainExternalCommand.add(command); }
    public boolean hasMainExternalCommand()                  { return ! this.mainExternalCommand.isEmpty(); }
    public List<CustomizeCommand<?>> getMainExternalCommand(){ return this.mainExternalCommand; }

    private boolean setHelpCommand                           = false;
    public boolean isHelpCommand()                           { return this.setHelpCommand; }
    protected void setHelpCommandType(boolean helpEnabled)   { this.setHelpCommand = helpEnabled; }

    private boolean enableConsole                            = true;
    protected void setEnabledConsoleSender(boolean enable)   { this.enableConsole = enable; }
    public boolean isEnabledConsoleSender()                  { return this.enableConsole; }

    private boolean enablePlayer                             = true;
    protected void setEnabledPlayer(boolean enable)          { this.enablePlayer = enable; }
    public boolean isEnabledPlayer()                         { return this.enablePlayer;  }

    public boolean isAvailableCommand(CommandSender sender)
    {
        if(sender instanceof Player)
            return this.enablePlayer;
        else if(sender instanceof ConsoleCommandSender)
            return this.enableConsole;
        else
            return false;
    }

    /**
     * The information of the superclass. This is automatically specified when the parent class registers child information.
     * <b>Changing the arbitrarily cause unexpected commands to working.</b>
     */
    private CustomizeCommand<?> parent                            = null;
    protected void setParent(@Nonnull CustomizeCommand<?> parent) { this.parent = parent; }
    @Nullable public CustomizeCommand<?> getParent()              { return this.parent; }
    public boolean isRoot()                                       { return this.parent == null; }
    public boolean hasPerent()                                    { return this.parent != null; }

    public String getRelativeCommand()                            { return this.getRelativeCommand(this, null, false); }
    public String getRelativeCommand(boolean main)                { return this.getRelativeCommand(this, null, main); }
    private String getRelativeCommand(CustomizeCommand<?> command, String label, boolean main)
    {
        if(command == null) throw new RuntimeException("command cannot be null");

        if(command.isRoot())
        {
            if(label == null)
                label = command.getAllCommands();
            else
                label = command.getMainCommand() + " " + label;
        }
        else
        {
            if(command.hasChild())
            {
                if(main)
                {
                    if(label == null)
                        label = command.getAllCommands();
                    else
                        label = command.getAllCommands() + " " + label;
                }
                else
                {
                    if(label == null)
                        label = command.getMainCommand();
                    else
                        label = command.getMainCommand() + " " + label;
                }
            }
            else
            {
                if(label == null)
                    label = command.getAllCommands();
                else
                    label = command.getAllCommands() + " " + label;
            }
        }
        if(command.isRoot()) return label;
        return this.getRelativeCommand(command.getParent(), label, false);
    }

    protected List<String> aliases                           = new ArrayList<String>();
    public boolean hasAliases()                              { return !this.aliases.isEmpty(); }
    public boolean isAlias(String cmd)                       { return this.aliases.contains(cmd); }
    public List<String> getAliases()                         { return this.aliases; }
    public void addAliases(String s)
    {
        if(this.getAliases().contains(s)) return;
        List<String> aliases = this.getAliases();
        aliases.add(s);
    }
    public void removeAliases(String s)
    {
        if(! this.getAliases().contains(s)) return;
        List<String> aliases = this.getAliases();
        aliases.remove(s);
    }
    public void addAliases(String... strings)
    {
        for(String s : strings)
        {
            if(this.aliases.contains(s)) continue;
            this.aliases.add(s);
        }
    }

    protected List<CustomizeCommand<?>> children                 = new ArrayList<CustomizeCommand<?>>();
    public boolean isFinal()                                     { return this.children.isEmpty(); }
    public boolean hasChild()                                    { return !this.isFinal(); }
    public List<CustomizeCommand<?>> getChild()                  { return this.children; }
    public CustomizeCommand<?> addChild(CustomizeCommand<?> cmd)
    {
        if(cmd == null) return null;
        cmd.setParent(this);
        if(this.hasPermission())
        {
            // Preventing for ConcurrentModificationException
            List<CustomizeCommand<?>> temp = new ArrayList<CustomizeCommand<?>>();
            for(CustomizeCommand<?> child : cmd.getChild())
            {
                if(child.hasPermission())
                    child.setPermission(this.getPermission() +  "." + child.getPermission());
                temp.add(child);
            }
            cmd.children = temp;
            if(cmd.hasPermission())
                cmd.setPermission(this.getPermission() + "." + cmd.getPermission());
        }
        if(this.getChild().indexOf(cmd) != -1)
        {
            this.children.remove(cmd);
        }
        this.children.add(cmd);
        this.setHelpCommand = this.hasChild();
        return this.children.get(this.children.indexOf(cmd));
    }

    /**
     *
     * @param cmd
     * @param index
     * @return
     */
    public CustomizeCommand<?> setChild(CustomizeCommand<?> cmd, int index)
    {
        if(cmd == null) return null;
        cmd.setParent(this);
        if(this.hasPermission())
        {
            // Preventing for ConcurrentModificationException
            List<CustomizeCommand<?>> temp = new ArrayList<CustomizeCommand<?>>();
            for(CustomizeCommand<?> child : cmd.getChild())
            {
                child.setPermission(this.getPermission() +  "." + child.getPermission());
                temp.add(child);
            }
            cmd.children = temp;
            cmd.setPermission(this.getPermission() + "." + cmd.getPermission());
        }
        if(index < 0)
            return this.addChild(cmd);
        else
            this.children.set(index, cmd);

        this.setHelpCommand = this.hasChild();
        return this.children.get(this.children.indexOf(cmd));
    }

    /**
     *
     * @param cmd
     * @return
     */
    public CustomizeCommand<?> removeChild(CustomizeCommand<?> cmd)
    {
        this.children.remove(cmd);
        this.setHelpCommand = this.hasChild();
        return cmd;
    }

    /**
     *
     * @param index
     * @return
     */
    public CustomizeCommand<?> removeChild(int index)
    {
        this.children.remove(index);
        this.setHelpCommand = this.hasChild();
        return this.getChild().get(index);
    }

    /**
     * Returns all of these commands. This is used to display the Help page. <b>Never use it when calling a command.</b>
     * @return The alias of all commands as a String
     */
    public String getAllCommands()
    {
        String s = "";
        Iterator<String> Iter = this.aliases.iterator();
                while(Iter.hasNext())
                {
                    String s2 = Iter.next();
                    s = s + s2;
                    if(Iter.hasNext())
                s = s + ",";
        }
        return s;
    }

    /**
     *
     * @return
     */
    public String getRootCommand()
    {
        CustomizeCommand<?> command = this;
        while(! command.isRoot())
        {
            command = command.getParent();
        }
        return command.getMainCommand();
    }

    /**
     *
     * @param sender
     * @param message
     * @param detail
     */
    @SuppressWarnings("unchecked")
    public void sendDetailMessage(CommandSender sender, String message, List<String> detail)
    {
        FancyMessage fm = new FancyMessage(message);
        Iterable<String> s = (Iterable<String>) detail.iterator();
        fm = fm.tooltip(s);
        if(sender instanceof Player)
            fm.send(sender);
        else if(sender instanceof ConsoleCommandSender)
            sender.sendMessage(message);
        else
            throw new ClassCastException("The sender's class type doesn't support");
    }

    /**
     * Decide how many commands to display on a page.
     */
    public static transient final int PAGE_SIZE = 7;

    /**
     * Displays one page of the command list. This method is showing the main page.
     * If you want to see a different page, refer to {@link #getHelp(CommandSender, int)}.
     * @param sender Target to show the page
     */
    public void getHelp(CommandSender sender)
    {
        List<CustomizeCommand<?>> commands = new ArrayList<CustomizeCommand<?>>();
            commands.addAll(this.mainExternalCommand);
            commands.addAll(this.children);
            if(commands.size() != 0)
            {
            ArrayList<FancyMessage> texts = new ArrayList<FancyMessage>();

            // Number of checked pages
            int max_page;
            if(sender instanceof ConsoleCommandSender)
            {
                max_page = 1;
            }
            else
            {
                max_page = (commands.size() / (PAGE_SIZE - 1)) + 1;
            }

            // Showing the top of page
            FancyMessage message = new FancyMessage(StringUtil.Color(
                    StringUtil.replaceValue("&e====&f [&b Help commands for &e\"{0}\" &a1/{1} &bpage(s) &f] &e====", this.getMainCommand(), max_page)));
            texts.add(message);

            //////////////////////////////// M A I N C O M M A N D ///////////////////////////////////
                                  // This is the command at the top of the page. //

            String mainCommand = this.getRelativeCommand(true);
            if(this.hasParameter())
            {
                // Add comment used parameters
                for(Parameter p : this.getParameter())
                {
                    if(p.isAvailable(sender))
                        mainCommand = mainCommand + " " + p.getParamString();
                }
            }
            message = new FancyMessage(StringUtil.Color("&6/" + mainCommand + " help,? &8[page]&f :&b " + this.getDescription()));
            if(this.hasDetailDescription())
            {
                // Add a user-defined detail description
                String[] str = this.getDetailDescription().toArray(new String[this.getDetailDescription().size()]);
                message.tooltip(str);
            }
            texts.add(message);
            //////////////////////////////////////////////////////////////////////////////////////////

            ////////////////////////////// O T H E R C O M M A N D S /////////////////////////////////
            // How many showing the command?
            int i;
            if(sender instanceof ConsoleCommandSender)
            {
                // The console can show all commands.
                // Therefore, the number of commands that can be displayed is the same as the number of registered commands.
                i = commands.size();
            }
            else
            {
                i = commands.size() >= PAGE_SIZE ? PAGE_SIZE - 1 : commands.size();
            }
            for(int j = 0; j < i; j++)
            {
                CustomizeCommand<?> command = commands.get(j);
                String s2 = command.getRelativeCommand(true);
                if(command.hasParameter())
                {
                    for(Parameter p : command.getParameter())
                    {
                        if(p.isAvailable(sender))
                            s2 = s2 +  " " + p.getParamString();
                    }
                }
                message = new FancyMessage(StringUtil.Color("&6/" + s2 + "&f :&b " + command.getDescription()));
                if(command.hasDetailDescription())
                {
                    String[] str = command.getDetailDescription().toArray(new String[command.getDetailDescription().size()]);
                    message.tooltip(str);
                }
                texts.add(message);
            }
            for(FancyMessage e : texts)
            {
                e.send(sender);
            }
            //////////////////////////////////////////////////////////////////////////////////////////
        }
        else
        {
            Msg.sendTxt(sender, "&cSorry! The command \"{0}\"\'s help is not provided because of no available commands.", this.getMainCommand());
        }
    }

    /**
     *
     * @param sender
     * @param page
     */
    public void getHelp(CommandSender sender, int page)
    {
        List<CustomizeCommand<?>> commands = new ArrayList<CustomizeCommand<?>>();
        commands.addAll(this.mainExternalCommand);
        commands.addAll(this.children);
        if(commands.size() != 0)
        {
            if(page == 1)
            {
                this.getHelp(sender);
                return;
            }

            int max_page = (commands.size() / (PAGE_SIZE -1)) + 1;
            if(page > max_page || page <= 0)
            {
                Msg.sendTxt(sender, "&cSorry! The command \"{0}\"\'s help of {1} page(s) is not provided", this.getMainCommand(), page);
                return;
            }

            ArrayList<FancyMessage> texts = new ArrayList<FancyMessage>();
            // Showing the top of page
            FancyMessage message = new FancyMessage(StringUtil.Color(StringUtil.replaceValue(
                    "&e====&f [&b Help commands for &e\"{0}\" &a{1}/{2} &bpage(s) &f] &e====", this.getMainCommand(), page, max_page)));
            texts.add(message);

            // Limited count
            int i = commands.size() >= (PAGE_SIZE * page) - 1 ? (PAGE_SIZE * page) - 1 : commands.size();

            // starting index number of command size
            int j = (PAGE_SIZE * (page - 1)) - 1;
            for(; j < i; j++)
            {
                CustomizeCommand<?> command = commands.get(j);
                String s2 = command.getRelativeCommand();
                if(command.hasParameter())
                {
                    for(Parameter p : command.getParameter())
                    {
                        if(p.isAvailable(sender))
                            s2 = s2 +  " " + p.getParamString();
                    }
                }
                message = new FancyMessage(StringUtil.Color("&6/" + s2 + "&f :&b " + command.getDescription()));
                if(command.hasDetailDescription())
                {
                    String[] str = command.getDetailDescription().toArray(new String[command.getDetailDescription().size()]);
                    message.tooltip(str);
                }
                texts.add(message);
            }

            for(FancyMessage e : texts)
            {
                e.send(sender);
            }
        }
        else
        {
            Msg.sendTxt(sender, "&cSorry! The command \"{0}\"\'s help is not provided.", this.getMainCommand());
        }
    }

    /**
     * This allows the command to be executed abstractly. the command will not run normally without this.
     * <b>Do not override this.</b> Overrides {@link #perform(CommandSender, List)} to reconstruct.
     * @param sender Source of the command
     * @param args Passed command arguments, List type
     * @return Whether the command was executed normally
     */
    public boolean execute(CommandSender sender, List<String> args)
    {
        if(args.size() == 0)
        {
            if(this.hasChild())
            {
                this.getHelp(sender);
                return true;
            }
            else
            {
                if(this.hasRequirementParam())
                {
                    Msg.sendTxt(sender, "&cThe param \"" + this.getParameter().get(0).getParamString() + "&c\" is required value!");
                    return false;
                }
                if(sender instanceof Player)
                {
                    if(! sender.hasPermission(this.getPermission()))
                    {
                        Msg.sendTxt(sender, "&cYou are not authorized for this command. &8({0})", this.getPermission());
                        return false;
                    }
                }
                return this.perform(sender, args);
            }
        }
        else
        {
            if(args.get(0).equalsIgnoreCase("?") || args.get(0).equalsIgnoreCase("help"))
            {
                if(this.hasChild() && this.isHelpCommand())
                {
                    if(args.size() >= 2 && StringUtil.isNumber(args.get(1)))
                    {
                        this.getHelp(sender, Integer.parseInt(args.get(1)));
                        return true;
                    }
                    this.getHelp(sender);
                    return true;
                }
                if(! sender.hasPermission(this.getPermission()))
                {
                    Msg.sendTxt(sender, "&cYou are not authorized for this command. &8({0})", this.getPermission());
                    return false;
                }
                this.perform(sender, args);
            }
            else
            {
                if(! this.hasChild())
                {
                    if(this.hasParameter())
                    {
                        if(this.getParameter().size() >= 1)
                        {
                            if(this.getParameter().size() < args.size())
                            {
                                String s = this.getRelativeCommand();
                                for(Parameter p : this.getParameter())
                                    s = s +" " + p.getParamString();

                                Msg.sendTxt(sender, "&cArgument limit exceeded:&f \"{0}\"", args.get(this.getParameter().size()));
                                Msg.sendTxt(sender, "&6Use Command:&f /{0}", s);
                                return false;
                            }
                            int count = 0;
                            for(Parameter p : this.getParameter())
                            {
                                if(p.isRequirement())
                                {
                                    if(args.size() <= count)
                                    {
                                        Msg.sendTxt(sender, "&cThe param \"" + p.getParamString() + "&c\" is required value!");
                                        return false;
                                    }
                                }
                                count++;
                            }
                            if(! sender.hasPermission(this.getPermission()))
                            {
                                Msg.sendTxt(sender, "&cYou are not authorized for this command. &8{0}", this.getPermission());
                                return false;
                            }
                            return this.perform(sender, args);
                        }
                    }
                    else
                    {

                    }
                    this.getActivePlugin().getLangConfiguration().sendMessage(sender, "System.UNKNOWN_COMMAND", "&b/" +  this.getRelativeCommand(true) + " ?");
                    return false;
                }
                else
                {
                    for(CustomizeCommand<?> c : this.getChild())
                    {
                        if(c.getMainCommand().equalsIgnoreCase(args.get(0)) || c.isAlias(args.get(0)))
                        {
                            List<String> argList = new ArrayList<String>(args);
                            argList.remove(0);
                            return c.execute(sender, argList);
                        }
                    }
                    this.getActivePlugin().getLangConfiguration().sendMessage(sender, "System.UNKNOWN_COMMAND", "&e/" +  this.getRelativeCommand(true) + " ?");
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean perform(CommandSender sender, List<String> args) { return false; }
}
