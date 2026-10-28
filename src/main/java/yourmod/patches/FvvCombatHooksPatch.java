package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import yourmod.powers.PassiveAggressionPower;
import yourmod.powers.SkipNextDrawPower;
import yourmod.util.FvvCombatState;

/**
 * fvv 战斗钩子。
 * 注意：不要用带 DamageInfo 等参数的 Prefix/Postfix 去补 AbstractCreature.damage，
 * 桌面版无 LocalVariableTable 时 ParamInfo.findName 会 NPE。
 */
public class FvvCombatHooksPatch {

    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfCombatLogic")
    public static class BattleStart {
        public static void Postfix(AbstractPlayer __instance) {
            FvvCombatState.onBattleStart();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfTurnRelics")
    public static class TurnStart {
        @SpirePrefixPatch
        public static void Prefix(AbstractPlayer __instance) {
            FvvCombatState.onPlayerTurnStart();
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "applyEndOfTurnTriggers")
    public static class EndTurn {
        public static void Postfix(AbstractCreature __instance) {
            if (__instance != null && __instance.isPlayer) {
                FvvCombatState.restoreUselessStatTax(__instance);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "useCard",
            paramtypez = {AbstractCard.class, AbstractMonster.class, int.class})
    public static class UseCard {
        public static void Postfix(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
            FvvCombatState.onCardPlayed(c);
            // 不补 damage()：攻击牌打出即记一次「本回合造过伤」（整活够用）
            if (c != null && c.type == AbstractCard.CardType.ATTACK) {
                FvvCombatState.damageDealtThisTurn += Math.max(1, c.damage);
            }
        }
    }

    /** Raw 插入，不依赖参数名调试表 */
    @SpirePatch(clz = AbstractCreature.class, method = "addBlock", paramtypez = {int.class})
    public static class BlockGain {
        public static void Raw(CtBehavior ctMethodToPatch) throws Exception {
            ctMethodToPatch.insertBefore(
                    "if ($0.isPlayer && $1 > 0) {"
                            + FvvCombatState.class.getName() + ".blockGainedThisTurn += $1;"
                            + "}"
            );
        }
    }

    @SpirePatch(clz = DrawCardAction.class, method = "update")
    public static class SkipDraw {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(DrawCardAction __instance) {
            AbstractPlayer p = AbstractDungeon.player;
            if (p == null) {
                return SpireReturn.Continue();
            }
            AbstractPower pow = p.getPower(SkipNextDrawPower.POWER_ID);
            if (pow != null && pow.amount > 0) {
                pow.flash();
                pow.amount--;
                if (pow.amount <= 0) {
                    p.powers.remove(pow);
                } else {
                    pow.updateDescription();
                }
                __instance.isDone = true;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    public static void onDiscardHook() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null) {
            return;
        }
        AbstractPower pow = p.getPower(PassiveAggressionPower.POWER_ID);
        if (pow instanceof PassiveAggressionPower) {
            ((PassiveAggressionPower) pow).onDiscard();
        }
    }
}
