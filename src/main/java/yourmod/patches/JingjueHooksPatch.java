package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import yourmod.JingjueCharacter;
import yourmod.cards.jingjue.JjBloodMark;
import yourmod.powers.jingjue.ExecuteForcePower;
import yourmod.util.JingjueCombatState;
import yourmod.util.JingjueEtherealStore;

public class JingjueHooksPatch {

    private static void ensureExecuteForce(AbstractPlayer p) {
        if (p != null && !p.hasPower(ExecuteForcePower.POWER_ID)) {
            p.addPower(new ExecuteForcePower(p));
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfCombatLogic")
    public static class BattleStart {
        public static void Prefix(AbstractPlayer __instance) {
            if (__instance instanceof JingjueCharacter) {
                JingjueCombatState.onBattleStart();
                JingjueEtherealStore.clear();
            }
        }

        public static void Postfix(AbstractPlayer __instance) {
            if (__instance instanceof JingjueCharacter) {
                ensureExecuteForce(__instance);
                JingjueCombatState.syncErosionPowers();
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfTurnRelics")
    public static class TurnStart {
        public static void Prefix(AbstractPlayer __instance) {
            if (!(__instance instanceof JingjueCharacter)) {
                return;
            }
            ensureExecuteForce(__instance);
            JingjueCombatState.onPlayerTurnStart();
            JingjueEtherealStore.atStartOfTurn();
            if (JingjueCombatState.nextTurnHeal > 0) {
                __instance.heal(JingjueCombatState.nextTurnHeal);
                JingjueCombatState.nextTurnHeal = 0;
            }
            if (JingjueCombatState.nextTurnDrawPenalty > 0) {
                __instance.gameHandSize -= JingjueCombatState.nextTurnDrawPenalty;
            }
            if (JingjueCombatState.keepEnergy > 0) {
                __instance.gainEnergy(JingjueCombatState.keepEnergy);
                JingjueCombatState.keepEnergy = 0;
            }
            if (JingjueCombatState.keepBlock > 0) {
                __instance.addBlock(JingjueCombatState.keepBlock);
                JingjueCombatState.keepBlock = 0;
            }
        }

        public static void Postfix(AbstractPlayer __instance) {
            if (__instance instanceof JingjueCharacter && JingjueCombatState.nextTurnDrawPenalty > 0) {
                __instance.gameHandSize += JingjueCombatState.nextTurnDrawPenalty;
                JingjueCombatState.nextTurnDrawPenalty = 0;
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage",
            paramtypez = {com.megacrit.cardcrawl.cards.DamageInfo.class})
    public static class TookDamage {
        public static int hp;

        public static void Prefix(AbstractPlayer __instance, com.megacrit.cardcrawl.cards.DamageInfo info) {
            hp = __instance.currentHealth;
        }

        public static void Postfix(AbstractPlayer __instance, com.megacrit.cardcrawl.cards.DamageInfo info) {
            if (__instance instanceof JingjueCharacter && __instance.currentHealth < hp) {
                JingjueCombatState.damageTakenThisTurn += hp - __instance.currentHealth;
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "moveToDiscardPile")
    public static class DiscardBlood {
        public static void Postfix(CardGroup __instance, AbstractCard c) {
            if (c instanceof JjBloodMark && AbstractDungeon.player instanceof JingjueCharacter) {
                JingjueCombatState.gainHourglass(1);
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "moveToExhaustPile")
    public static class ExhaustBlood {
        public static void Postfix(CardGroup __instance, AbstractCard c) {
            if (c instanceof JjBloodMark && AbstractDungeon.player instanceof JingjueCharacter) {
                JingjueCombatState.gainHourglass(1);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "draw", paramtypez = {})
    public static class DrawDiscount {
        public static void Postfix(AbstractPlayer __instance) {
            if (!(__instance instanceof JingjueCharacter) || JingjueCombatState.nextCardsDiscount <= 0) {
                return;
            }
            if (__instance.hand == null || __instance.hand.isEmpty()) {
                return;
            }
            AbstractCard c = __instance.hand.getTopCard();
            if (c != null && c.costForTurn >= 1) {
                c.setCostForTurn(c.costForTurn - 1);
                JingjueCombatState.nextCardsDiscount--;
            }
        }
    }

    @SpirePatch(clz = AbstractRoom.class, method = "endTurn")
    public static class EndTurnKeep {
        public static void Prefix(AbstractRoom __instance) {
            if (!(AbstractDungeon.player instanceof JingjueCharacter)) {
                return;
            }
            JingjueCombatState.captureVisibleIntents();
            JingjueCombatState.gainedHourglassThisTurn = false;
            if (JingjueCombatState.keepResources) {
                JingjueCombatState.keepEnergy = com.megacrit.cardcrawl.ui.panels.EnergyPanel.totalCount;
                JingjueCombatState.keepBlock = AbstractDungeon.player.currentBlock;
                JingjueCombatState.keepResources = false;
            }
        }
    }
}
