package io.github.kunonx.DesignFramework.system;

import io.github.kunonx.DesignFramework.SoundEffect;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public abstract class GraphicUserInterface
{
    private SoundEffect soundClick = SoundEffect.valueOf("ENTITY_EXPERIENCE_ORB_PICKUP", 0.75f, 1.0f);
    public SoundEffect getSoundClick() { return this.soundClick; }
    public void setSoundClick(SoundEffect soundClick) { this.soundClick = soundClick; }

    private SoundEffect soundOpen = SoundEffect.valueOf("BLOCK_CHEST_OPEN", 0.75f, 1.4f);
    public SoundEffect getSoundOpen() { return this.soundOpen; }
    public void setSoundOpen(SoundEffect soundOpen) { this.soundOpen = soundOpen; }

    private SoundEffect soundClose = SoundEffect.valueOf("BLOCK_CHEST_CLOSE", 0.75f, 1.4f);
    public SoundEffect getSoundClose() { return this.soundClose; }
    public void setSoundClose(SoundEffect soundClose) { this.soundClose= soundClose; }

    public Map<Integer, ItemStack> getRegisterCategory() { return this.getRegisterCategory(); }

    public Map<?, ?> getFunctionalizationSlots() { return this.getFunctionalizationSlots(); }

    public Inventory open() { return this.open(); }

    public int getTableSize() { return this.getTableSize(); }

    protected void Initialized()
    {

    }
}
