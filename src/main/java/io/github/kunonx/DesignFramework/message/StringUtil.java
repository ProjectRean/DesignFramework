package io.github.kunonx.DesignFramework.message;

import io.github.kunonx.DesignFramework.security.IntegrityChecker;
import org.bukkit.ChatColor;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class StringUtil
{
    public static String Color(String str) { return ChatColor.translateAlternateColorCodes('&', str); }

    public static String replaceValue(String str, Object... values)
    {
        if(values == null) return str;
        if(values.length == 0) return str;
            int i = 0;
            while(str.matches(".*\\{[0-9]\\}.*"))
            {
                int j = 0;
                String value = null;
                if(i >= values.length)
                {
                    j = values.length - 1;
                }
                else
                {
                    j = i;
                }
                if(values[j] instanceof String)
                {
                    value = (String)values[j];
                }
                else if(values[j] instanceof Number)
                {
                    double num = ((Number)values[i]).doubleValue();
                    if(num - ((int)num) == 0)
                    {
                        value = String.valueOf((int)num);
                    }
                    else
                    {
                        value = String.valueOf(num);
                    }
                }
                else if(values[j] instanceof Boolean)
                {
                value = String.valueOf((Boolean)values[j]);
                }
            else
            {
                value = String.valueOf(values[j]);
            }
            str = str.replaceAll("\\{" + String.valueOf(i) + "\\}", value);
            i++;
        }
        return str;
    }

    public static String getColorHash(String input)
    {
        String SHA = null;
        try
        {
            SHA = IntegrityChecker.sha1(input);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        if(SHA == null) return null;
        String hexCode = new String(new char[]{SHA.charAt(0), SHA.charAt(2), SHA.charAt(4), SHA.charAt(5), SHA.charAt(6)});
        int v = Math.abs((Integer.valueOf(hexCode, 16) / 16));
        return new String(new char[]{'&' , ChatColor.getByChar(String.valueOf(v).toCharArray()[0]).getChar()});
    }

    public static List<String> ColorStringList(List<String> l)
    {
        for(int i = 0; i < l.size(); i++)
        {
            String s = l.get(i);
            String reg = "&f" + s;
            l.set(i, ChatColor.translateAlternateColorCodes('&', reg));
        }
        return l;
    }

    public static boolean isNumber(String str)
    {
        try
        {
            Double.parseDouble(str);
            return true;
        }
        catch(NumberFormatException e)
        {
            return false;
        }
    }

    public static boolean isSymbolFormatted(String format, String value)
    {
        return value.equalsIgnoreCase(String.format(format, value));
    }

    public static String removeSymbolFormat(String format, String value)
    {
        String str = String.format(format, value);
        StringBuffer buffer = new StringBuffer();
        for(int i=0 ; i< str .length(); i++)
        {
            if(Character.isLetterOrDigit(str.charAt(i)))
                buffer.append(str.charAt(i));
        }
        return buffer.toString();
    }
}
