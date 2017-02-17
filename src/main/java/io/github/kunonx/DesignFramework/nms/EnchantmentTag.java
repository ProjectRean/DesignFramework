package io.github.kunonx.DesignFramework.nms;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.Collection;

public class EnchantmentTag
{
    private static boolean useItemFlags = false;
    private static boolean useReflection;

    private static Class<?> nbtTagCompoundClass;
    private static Class<?> nbtTagListClass;
    private static Class<?> nmsItemstackClass;
    private static Method asNmsCopyMethod;
    private static Method asCraftMirrorMethod;
    private static Method hasTagMethod;
    private static Method getTagMethod;
    private static Method setTagMethod;
    private static Method nbtSetMethod;


    public static boolean setup()
    {
        try
        {
            Class.forName("org.bukkit.inventory.ItemFlag");
            useItemFlags = true;
        }
        catch(ClassNotFoundException e)
        {
            try
            {
                // Try to get the NMS methods and classes
                nbtTagCompoundClass = getNmsClass("NBTTagCompound");
                nbtTagListClass = getNmsClass("NBTTagList");
                nmsItemstackClass = getNmsClass("ItemStack");

                asNmsCopyMethod = getObcClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class);
                asCraftMirrorMethod = getObcClass("inventory.CraftItemStack").getMethod("asCraftMirror", nmsItemstackClass);

                hasTagMethod = nmsItemstackClass.getMethod("hasTag");
                getTagMethod = nmsItemstackClass.getMethod("getTag");
                setTagMethod = nmsItemstackClass.getMethod("setTag", nbtTagCompoundClass);

                nbtSetMethod = nbtTagCompoundClass.getMethod("set", String.class, getNmsClass("NBTBase"));

                useReflection = true;

            }
            catch (Exception e2)
            {
                //Could not enable the attribute remover for this version (" + e + "). Attributes will show up on items.
            }
        }
        return true;
    }


    private static Class<?> getNmsClass(String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + getBukkitVersion() + "." + name);
    }

    private static Class<?> getObcClass(String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + getBukkitVersion() + "." + name);
    }

    public static ItemStack hideAttributes(ItemStack item)
    {
        if (item == null)
        {
            return null;
        }

        //
        item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);

        if (useItemFlags) {
            ItemMeta meta = item.getItemMeta();
            if (isNullOrEmpty(meta.getItemFlags())) {
                // Add them only if necessary
                meta.addItemFlags(ItemFlag.values());
                item.setItemMeta(meta);
            }
            return item;

        }
        else if (useReflection) {
            try {

                Object nmsItemstack = asNmsCopyMethod.invoke(null, item);
                if (nmsItemstack == null) {
                    return item;
                }

                Object nbtCompound;
                if ((Boolean) hasTagMethod.invoke(nmsItemstack)) {
                    nbtCompound = getTagMethod.invoke(nmsItemstack);
                } else {
                    nbtCompound = nbtTagCompoundClass.newInstance();
                    setTagMethod.invoke(nmsItemstack, nbtCompound);
                }

                if (nbtCompound == null) {
                    return item;
                }

                Object nbtList = nbtTagListClass.newInstance();
                nbtSetMethod.invoke(nbtCompound, "AttributeModifiers", nbtList);
                return (ItemStack) asCraftMirrorMethod.invoke(null, nmsItemstack);

            } catch (Exception e) {	}
        }

        // On failure
        return item;
    }

    private static boolean isNullOrEmpty(Collection<?> coll)
    {
        return coll == null || coll.isEmpty();
    }

    private static String bukkitVersion;

    public static String getBukkitVersion()
    {
        if (bukkitVersion == null)
        {
            String packageName = Bukkit.getServer().getClass().getPackage().getName();
            bukkitVersion = packageName.substring(packageName.lastIndexOf('.') + 1);
        }
        return bukkitVersion;
    }

}
