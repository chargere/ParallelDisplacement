package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.relics.VioletAurora;
import yourmod.util.DamuhouCombatState;

/**
 * 达穆候战斗钩子。棱镜倍率改挂在敌人能力上，悬停卡牌能看到 1.5 / 0.75。
 */
public class DamuhouCombatHooksPatch {

    @SpirePatch(clz = AbstractPlayer.class, method = "useCard",
            paramtypez = {AbstractCard.class, AbstractMonster.class, int.class})
    public static class AfterPlayAttack {
        public static void Postfix(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
            if (c == null) {
                return;
            }
            DamuhouCombatState.cardsPlayedThisTurn++;
            if (c.type == AbstractCard.CardType.ATTACK) {
                DamuhouCombatState.attacksPlayedThisTurn++;
                if (DamuhouCombatState.ammoRationActive) {
                    __instance.damage(new com.megacrit.cardcrawl.cards.DamageInfo(__instance, 1,
                            com.megacrit.cardcrawl.cards.DamageInfo.DamageType.HP_LOSS));
                }
            } else if (c.type == AbstractCard.CardType.SKILL) {
                DamuhouCombatState.skillsPlayedThisTurn++;
                if (DamuhouCombatState.holdNextCardAttackCombo) {
                    DamuhouCombatState.holdNextCardAttackCombo = false;
                } else {
                    DamuhouCombatState.nextCardAttackCombo = 0;
                }
                if (DamuhouCombatState.holdNextSkillRefund) {
                    DamuhouCombatState.holdNextSkillRefund = false;
                } else if (DamuhouCombatState.nextSkillCostRefund > 0) {
                    __instance.gainEnergy(DamuhouCombatState.nextSkillCostRefund);
                    DamuhouCombatState.nextSkillCostRefund = 0;
                }
            } else {
                if (DamuhouCombatState.holdNextCardAttackCombo) {
                    DamuhouCombatState.holdNextCardAttackCombo = false;
                } else {
                    DamuhouCombatState.nextCardAttackCombo = 0;
                }
            }
            if (DamuhouCombatState.cardsPlayedThisTurn == 1 && DamuhouCombatState.firstCardCostRefund > 0) {
                __instance.gainEnergy(DamuhouCombatState.firstCardCostRefund);
            }
            if (DamuhouCombatState.nthCardIndex > 0
                    && DamuhouCombatState.cardsPlayedThisTurn == DamuhouCombatState.nthCardIndex
                    && DamuhouCombatState.nthCardCostRefund > 0) {
                __instance.gainEnergy(DamuhouCombatState.nthCardCostRefund);
            }
            if (c.type != AbstractCard.CardType.ATTACK) {
                return;
            }
            if (__instance == null || !__instance.hasRelic(VioletAurora.ID)) {
                return;
            }
            DamuhouCombatState.beginAttackCard();
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    DamuhouCombatState.endAttackCard();
                    isDone = true;
                }
            });
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfTurnRelics")
    public static class PlayerTurnStart {
        public static void Prefix(AbstractPlayer __instance) {
            DamuhouCombatState.onPlayerTurnStart();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage",
            paramtypez = {com.megacrit.cardcrawl.cards.DamageInfo.class})
    public static class PlayerDamaged {
        public static int hp;

        public static void Prefix(AbstractPlayer __instance, com.megacrit.cardcrawl.cards.DamageInfo info) {
            hp = __instance.currentHealth;
        }

        public static void Postfix(AbstractPlayer __instance, com.megacrit.cardcrawl.cards.DamageInfo info) {
            if (__instance.currentHealth < hp) {
                DamuhouCombatState.playerDamagedThisTurn = true;
            }
        }
    }
}
