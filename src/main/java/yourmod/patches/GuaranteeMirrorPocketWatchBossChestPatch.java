package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.chests.BossChest;
import yourmod.DominicCharacter;
import yourmod.relics.AuroraPocketWatch;
import yourmod.relics.MirrorPocketWatch;

import java.util.ArrayList;

/**
 * 倒影怀表保底：
 * <ul>
 *   <li>Act1：50% 强制进入宝箱三选一</li>
 *   <li>Act2：若本局尚未出现过（仍在 Boss 池中），则必出</li>
 * </ul>
 */
@SpirePatch(clz = BossChest.class, method = SpirePatch.CONSTRUCTOR)
public class GuaranteeMirrorPocketWatchBossChestPatch {

    public static void Postfix(BossChest __instance) {
        if (AbstractDungeon.player == null
                || AbstractDungeon.player.chosenClass != DominicCharacter.Enums.THE_DOMINIC) {
            return;
        }
        int act = AbstractDungeon.actNum;
        if (act != 1 && act != 2) {
            return;
        }
        if (!AbstractDungeon.player.hasRelic(AuroraPocketWatch.ID)
                || AbstractDungeon.player.hasRelic(MirrorPocketWatch.ID)) {
            return;
        }

        ArrayList<AbstractRelic> choices = __instance.relics;
        if (choices == null || choices.size() < 3) {
            return;
        }

        for (AbstractRelic r : choices) {
            if (r != null && MirrorPocketWatch.ID.equals(r.relicId)) {
                removeAllFromBossPool(MirrorPocketWatch.ID);
                return;
            }
        }

        ArrayList<String> pool = AbstractDungeon.bossRelicPool;
        if (pool == null || !pool.contains(MirrorPocketWatch.ID)) {
            return;
        }

        if (act == 1) {
            if (AbstractDungeon.relicRng == null || !AbstractDungeon.relicRng.randomBoolean()) {
                return;
            }
        }

        AbstractRelic mirror = RelicLibrary.getRelic(MirrorPocketWatch.ID).makeCopy();
        AbstractRelic displaced = choices.set(0, mirror);
        removeAllFromBossPool(MirrorPocketWatch.ID);
        if (displaced != null && displaced.relicId != null && !MirrorPocketWatch.ID.equals(displaced.relicId)) {
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
