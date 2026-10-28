package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import yourmod.CharacterFile;
import yourmod.relics.EternalStormAnchor;

import java.util.ArrayList;

/**
 * 洗牌后把已解锁的「永恒风暴的锚点」略提前，减轻共享 Boss 池稀释。
 * 无人怜悯改为由 {@link GuaranteeNoMercyBossChestPatch} 在一二阶段保底，不再堆权重。
 */
@SpirePatch(clz = AbstractDungeon.class, method = "initializeRelicList")
public class BossRelicPoolWeightPatch {
    private static final int EXTRA_COPIES = 1;

    public static void Postfix(AbstractDungeon __instance) {
        if (AbstractDungeon.player == null
                || AbstractDungeon.player.chosenClass != CharacterFile.Enums.THE_TODO) {
            return;
        }
        ArrayList<String> pool = AbstractDungeon.bossRelicPool;
        if (pool == null || pool.isEmpty()) {
            return;
        }
        if (!UnlockTracker.isRelicLocked(EternalStormAnchor.ID)) {
            bumpToFront(pool, EternalStormAnchor.ID);
        }
    }

    private static void bumpToFront(ArrayList<String> pool, String id) {
        if (!pool.contains(id)) {
            return;
        }
        while (pool.remove(id)) {
            // remove all existing copies
        }
        for (int i = 0; i < 1 + EXTRA_COPIES; i++) {
            pool.add(0, id);
        }
    }
}
