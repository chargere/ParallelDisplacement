package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.chests.BossChest;
import yourmod.CharacterFile;
import yourmod.relics.CompassionFire;
import yourmod.relics.NoMercy;

import java.util.ArrayList;

/**
 * 无人怜悯保底：
 * <ul>
 *   <li>Act1：50% 强制进入宝箱三选一</li>
 *   <li>Act2：若本局尚未出现过（仍在 Boss 池中），则必出</li>
 * </ul>
 * 一旦进入某次宝箱（自然滚到或强制塞入），从 Boss 池清掉全部副本，避免再刷。
 */
@SpirePatch(clz = BossChest.class, method = SpirePatch.CONSTRUCTOR)
public class GuaranteeNoMercyBossChestPatch {

    public static void Postfix(BossChest __instance) {
        if (AbstractDungeon.player == null
                || AbstractDungeon.player.chosenClass != CharacterFile.Enums.THE_TODO) {
            return;
        }
        int act = AbstractDungeon.actNum;
        if (act != 1 && act != 2) {
            return;
        }
        if (!AbstractDungeon.player.hasRelic(CompassionFire.ID)
                || AbstractDungeon.player.hasRelic(NoMercy.ID)) {
            return;
        }

        ArrayList<AbstractRelic> choices = __instance.relics;
        if (choices == null || choices.size() < 3) {
            return;
        }

        // 已自然滚到：清池，本局不再出现
        for (AbstractRelic r : choices) {
            if (r != null && NoMercy.ID.equals(r.relicId)) {
                removeAllFromBossPool(NoMercy.ID);
                return;
            }
        }

        ArrayList<String> pool = AbstractDungeon.bossRelicPool;
        // 池中已无 → 上一层已经出过（保底或自然），本层不再塞
        if (pool == null || !pool.contains(NoMercy.ID)) {
            return;
        }

        if (act == 1) {
            // 一层：50%（走 relicRng，跟种子）
            if (AbstractDungeon.relicRng == null || !AbstractDungeon.relicRng.randomBoolean()) {
                return;
            }
        }
        // act == 2：池中仍有 = 一层没出过 → 必出

        AbstractRelic noMercy = RelicLibrary.getRelic(NoMercy.ID).makeCopy();
        AbstractRelic displaced = choices.set(0, noMercy);
        removeAllFromBossPool(NoMercy.ID);
        if (displaced != null && displaced.relicId != null && !NoMercy.ID.equals(displaced.relicId)) {
            pool.add(displaced.relicId);
        }
    }

    private static void removeAllFromBossPool(String id) {
        ArrayList<String> pool = AbstractDungeon.bossRelicPool;
        if (pool == null) {
            return;
        }
        while (pool.remove(id)) {
            // remove every copy
        }
    }
}
