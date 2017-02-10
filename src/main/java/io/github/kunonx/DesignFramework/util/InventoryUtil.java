package io.github.kunonx.DesignFramework.util;

public class InventoryUtil
{
	public static int getSlot(int x, int y)
	{
		return (x-1) + (9 * (y - 1));
	}
}
