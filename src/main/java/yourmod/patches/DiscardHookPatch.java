package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import yourmod.util.HongwenHooks;

/**
 * 弃牌钩子：滤网静电、焚雷军令、星灰筛等。
 * - DiscardSpecificCardAction：点名弃置
 * - DiscardAction：选手牌弃置（星灰筛走这条，原先未挂钩）
 * - ScryAction：预见弃牌
 */
public class DiscardHookPatch {

    @SpirePatch(clz = DiscardSpecificCardAction.class, method = "update")
    public static class DiscardSpecific {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(DiscardSpecificCardAction __instance, AbstractCard ___targetCard) {
            HongwenHooks.onCardDiscarded(___targetCard);
            yourmod.util.DominicHooks.onCardDiscarded(___targetCard);
            FvvCombatHooksPatch.onDiscardHook();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractCard.class, "triggerOnManualDiscard");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    /** 星灰筛等：DiscardAction 只调 triggerOnManualDiscard，原先未进钩子 */
    @SpirePatch(clz = DiscardAction.class, method = "update")
    public static class HandDiscard {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractCard.class.getName())
                            && m.getMethodName().equals("triggerOnManualDiscard")) {
                        m.replace("{"
                                + "$proceed($$);"
                                + "yourmod.util.HongwenHooks.onCardDiscarded($0);"
                                + "yourmod.util.DominicHooks.onCardDiscarded($0);"
                                + "yourmod.patches.FvvCombatHooksPatch.onDiscardHook();"
                                + "}");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = ScryAction.class, method = "update")
    public static class ScryDiscard {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(CardGroup.class.getName())
                            && m.getMethodName().equals("moveToDiscardPile")) {
                        m.replace("{"
                                + "$proceed($$);"
                                + "yourmod.util.HongwenHooks.onCardDiscarded($1);"
                                + "yourmod.util.DominicHooks.onCardDiscarded($1);"
                                + "yourmod.patches.FvvCombatHooksPatch.onDiscardHook();"
                                + "}");
                    }
                }
            };
        }
    }
}
