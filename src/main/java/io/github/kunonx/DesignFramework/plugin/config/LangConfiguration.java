package io.github.kunonx.DesignFramework.plugin.config;

import com.google.common.base.Charsets;

import io.github.kunonx.DesignFramework.SyncConfigFileReader;
import io.github.kunonx.DesignFramework.json.JSONObject;
import io.github.kunonx.DesignFramework.message.Msg;
import io.github.kunonx.DesignFramework.message.StringUtil;
import io.github.kunonx.DesignFramework.plugin.DesignFrameworkPlugin;

import org.bukkit.command.CommandSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LangConfiguration extends SyncConfigFileReader
{
    private JSONObject JSON_OBJECT;
    public JSONObject getJsonObject() { return this.JSON_OBJECT; };

    @Override
    public void refresh()
    {
        this.JSON_OBJECT = new JSONObject(readFile(this.file.getAbsolutePath()));
    }

    public LangConfiguration()
    {

    }

    public LangConfiguration load(DesignFrameworkPlugin plugin)
    {
        return this.load(plugin.getLocale(), plugin);
    }

    public LangConfiguration load(Locale locale, DesignFrameworkPlugin plugin)
    {
        return this.load(locale.getCountry(), plugin);
    }

    public LangConfiguration load(String locale, DesignFrameworkPlugin plugin)
    {
        if (locale == null) locale = "ko";
        File dir = new File(plugin.getDataFolder(), "lang/");
        if (!dir.exists())
        {
            Msg.console(plugin, "&cLanguage directory not found! Creating the directory");
            dir.mkdirs();
        }
        File file = new File(dir, locale + "_lang.json");
        File original_file = new File(plugin.getDataFolder(), locale + "_lang.json");
        try
        {
        	//System.out.println(original_file.getName());
        	//System.out.println(original_file.exists());
            if (!original_file.exists())
            {
                original_file.createNewFile();
	            synchronized (file)
	            {
	                Msg.console(plugin, "&aUsing locale: &f" + locale);
	                try
	                {
	                    plugin.saveResource(locale + "_lang.json", true);
	                }
	                catch (IllegalArgumentException e)
	                {
	                    Msg.console(plugin, "&fThe language (" + locale + ") file not found in JARs, you want to customize it? Support this plugin developer.");
	                    return load("ko", plugin);
	                }
	            }
	            FileInputStream inputStream = new FileInputStream(original_file.getAbsolutePath());
	            FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
	
	            FileChannel fcin = inputStream.getChannel();
	            FileChannel fcout = outputStream.getChannel();
	
	            long size = fcin.size();
	            fcin.transferTo(0, size, fcout);
	
	            fcout.close();
	            fcin.close();
	
	            outputStream.close();
	            inputStream.close();
	
	            original_file.delete();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        LangConfiguration lc = new LangConfiguration();
        lc.JSON_OBJECT = new JSONObject(readFile(file.getAbsolutePath()));
        lc.file = file;
        lc.setEnabled(plugin);
        return lc;
    }

    private String readFile(String filename)
    {
        String result = "";
        try
        {
            FileInputStream fis = new FileInputStream(new File(filename));
            InputStreamReader isr = new InputStreamReader(fis, Charsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null)
            {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
            br.close();
        }
        catch(FileNotFoundException e)
        {
            Msg.console(this.activePlugin, "&cThe filename {0} not found!", filename.replaceAll("\\\\", "/"));
            Msg.console(this.activePlugin, "&fPlease reload using the command: &6/df reload -c LangConfiguration");
            Msg.console(this.activePlugin, "&cLangConfiguration path: {0} was deactivated.", filename.replaceAll("\\\\", "/"));
            this.setEnabled(false);
        }
        catch(IOException e)
        {

        }
        return result;
    }

    public String getMessage(String path)
    {
        JSONObject j = this.getJsonObject();
        path = path.replaceAll("\\.", "@");
        if(path.contains("@"))
        {
            List<String> st = Arrays.asList(path.split("@"));
            for(String s : st)
            {
                if(st.size() == st.indexOf(s) + 1)
                {
                    return j.getString(s);
                }
                else
                {
                    j = j.getJSONObject(s);
                }
            }
        }
        else
        {
            return (String) j.get(path);
        }
        return null;
    }

    public void sendMessage(CommandSender sender, String key, Object... values) { this.sendMessage(sender, key, values, true); }

    public void sendMessage(CommandSender sender, String key, Object[] values, boolean pluginPrefix)
    {
        if(pluginPrefix)
            Msg.sendTxt(activePlugin, sender, StringUtil.replaceValue(this.getMessage(key), values));
        else
            Msg.sendTxt(sender, StringUtil.replaceValue(this.getMessage(key), values));

    }
}
