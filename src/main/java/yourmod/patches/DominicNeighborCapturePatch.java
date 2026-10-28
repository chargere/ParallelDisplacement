package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DominicCombatState;

/**
 * 打牌前捕获手牌邻牌，供迟滞使用。
 */
@SpirePatch(clz = AbstractPlayer.class, method = "useCard")
public class DominicNeighborCapturePatch {
    @SpirePrefixPatch
    public static void Prefix(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
        DominicCombatState.clearNeighbors();
        if (__instance.hand == null || c == null) {
            return;
        }
        int idx = __instance.hand.group.indexOf(c);
        if (idx < 0) {
            return;
        }
        if (idx > 0) {
            DominicCombatState.neighborLeft = __instance.hand.group.get(idx - 1);
        }
        if (idx < __instance.hand.group.size() - 1) {
            DominicCombatState.neighborRight = __instance.hand.group.get(idx + 1);
        }
    }
}
