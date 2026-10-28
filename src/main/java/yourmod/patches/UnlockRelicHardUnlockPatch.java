package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import yourmod.ModFile;

/**
 * 原版 {@link UnlockTracker#hardUnlockOverride} 只写 unlockPref=2，
 * 不会从 {@link UnlockTracker#lockedRelics} 移除 ID。
 * <p>
 * {@link UnlockTracker#isRelicLocked} 只查 lockedRelics 列表，
 * 所以解锁仪式后同一会话里，待解锁遗物仍会被 PopulatePools 跳过，进不了 Boss 池。
 */
@SpirePatch(clz = UnlockTracker.class, method = "hardUnlockOverride")
public class UnlockRelicHardUnlockPatch {
    public static void Postfix(String key) {
        if (key != null && key.startsWith(ModFile.modID + ":")) {
            // addRelic/addCard 可能重复插入；remove 只删一个，必须删光
            while (UnlockTracker.lockedRelics.remove(key)) {
                // keep removing
            }
            while (UnlockTracker.lockedCards.remove(key)) {
                // keep removing
            }
        }
    }
}
