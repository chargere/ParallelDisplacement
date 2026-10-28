package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import yourmod.util.DominicHooks;

@SpirePatch(clz = AbstractCard.class, method = "triggerOnExhaust")
public class DominicExhaustHookPatch {
    public static void Postfix(AbstractCard __instance) {
        DominicHooks.onCardExhausted(__instance);
    }
}
