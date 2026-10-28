package yourmod.util;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import yourmod.relics.ClosedStarChart;
import yourmod.relics.HatchFrost;
import yourmod.relics.MirrorPocketWatch;
import yourmod.actions.FrostPayGateAction;
import yourmod.cards.dominic.FrostPayType;
import yourmod.powers.CreditorPower;
import yourmod.powers.FrostBiteMarkPower;
import yourmod.powers.FrostCapRiftPower;
import yourmod.powers.FrostColdPower;
import yourmod.powers.FrostPayDiscountPower;
import yourmod.powers.FrostRidgePower;
import yourmod.powers.IceDustEchoPower;
import yourmod.powers.RefinedAshFormPower;
import yourmod.powers.StarMarkPower;
import yourmod.powers.StarMarkSensePower;
import yourmod.powers.StardustSettlePower;

import static yourmod.util.Wiz.atb;

/**
 * 多米尼克机制钩子：霜偿、耗霜、冰尘、星痕、弃牌等。
 */
public class DominicHooks {

    public static int modifyFrostPayCancelCost(AbstractPlayer p, int cost) {
        if (p == null || cost <= 0) {
            return cost;
        }
        int result = cost;
        AbstractPower disc = p.getPower(FrostPayDiscountPower.POWER_ID);
        if (disc != null && !DominicCombatState.frostPayDiscountUsedThisTurn) {
            DominicCombatState.frostPayDiscountUsedThisTurn = true;
            result = Math.max(1, result - disc.amount);
        }
        if (DominicCombatState.auroraCommandDiscountsLeft > 0) {
            DominicCombatState.auroraCommandDiscountsLeft--;
            result = Math.max(1, result - 1);
        }
        if (p.hasRelic(MirrorPocketWatch.ID) && DominicCombatState.mirrorWatchCancelAvailableThisTurn) {
            DominicCombatState.mirrorWatchCancelAvailableThisTurn = false;
            com.megacrit.cardcrawl.relics.AbstractRelic relic = p.getRelic(MirrorPocketWatch.ID);
            if (relic != null) {
                relic.flash();
            }
            result = Math.max(1, result - 1);
        }
        return result;
    }

    public static void onFrostPayCancelled(AbstractPlayer p) {
        DominicCombatState.frostPayCancelledThisTurn++;
        DominicCombatState.lastFrostPayCancelled = true;
        AbstractPower ridge = p.getPower(FrostRidgePower.POWER_ID);
        if (ridge != null) {
            ridge.flash();
            atb(new GainBlockAction(p, p, ridge.amount));
        }
        AbstractPower creditor = p.getPower(CreditorPower.POWER_ID);
        if (creditor instanceof CreditorPower) {
            ((CreditorPower) creditor).onCancel();
        }
        if (p.hasRelic(ClosedStarChart.ID) && !DominicCombatState.closedStarChartUsedThisCombat) {
            DominicCombatState.closedStarChartUsedThisCombat = true;
            com.megacrit.cardcrawl.relics.AbstractRelic relic = p.getRelic(ClosedStarChart.ID);
            if (relic != null) {
                relic.flash();
            }
            atb(new DrawCardAction(p, 2));
        }
    }

    public static void onFrostPayAccepted(AbstractPlayer p) {
        DominicCombatState.frostPayAcceptedThisTurn++;
        DominicCombatState.lastFrostPayCancelled = false;
        AbstractPower ledger = p.getPower(yourmod.powers.DebtLedgerPower.POWER_ID);
        if (ledger instanceof yourmod.powers.DebtLedgerPower) {
            yourmod.powers.DebtLedgerPower dl = (yourmod.powers.DebtLedgerPower) ledger;
            int max = dl.isTwoAmount ? Math.max(1, dl.amount2) : 1;
            if (DominicCombatState.debtLedgerTriggersThisTurn < max) {
                DominicCombatState.debtLedgerTriggersThisTurn++;
                DominicCombatState.debtLedgerDrawNextTurn += dl.amount;
            }
        }
        AbstractPower creditor = p.getPower(CreditorPower.POWER_ID);
        if (creditor instanceof CreditorPower) {
            ((CreditorPower) creditor).onAccept();
        }
        if (p.hasRelic(HatchFrost.ID) && DominicCombatState.hatchFrostHealsThisCombat < 2) {
            DominicCombatState.hatchFrostHealsThisCombat++;
            com.megacrit.cardcrawl.relics.AbstractRelic relic = p.getRelic(HatchFrost.ID);
            if (relic != null) {
                relic.flash();
            }
            p.heal(2, true);
        }
    }

    /** 主动消耗霜寒（取消霜偿、霜噬、融雪等） */
    public static void onActiveFrostSpend(AbstractPlayer p, int amount) {
        if (p == null || amount <= 0) {
            return;
        }
        AbstractPower mark = p.getPower(FrostBiteMarkPower.POWER_ID);
        if (mark != null && DominicCombatState.frostBiteMarkTriggersThisTurn < mark.amount) {
            DominicCombatState.frostBiteMarkTriggersThisTurn++;
            mark.flash();
            AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            if (m != null) {
                atb(new ApplyPowerAction(m, p, new StarMarkPower(m, 1), 1));
            }
        }
    }

    public static void onIceDust(AbstractPlayer p) {
        if (p == null) {
            return;
        }
        AbstractPower echo = p.getPower(IceDustEchoPower.POWER_ID);
        if (echo != null) {
            echo.flash();
            atb(new GainBlockAction(p, p, echo.amount));
        }
        AbstractPower settle = p.getPower(StardustSettlePower.POWER_ID);
        if (settle instanceof StardustSettlePower) {
            ((StardustSettlePower) settle).onIceDust();
        }
    }

    public static void onFrostReachedCap(AbstractCreature owner) {
        if (!(owner instanceof AbstractPlayer)) {
            return;
        }
        AbstractPlayer p = (AbstractPlayer) owner;
        AbstractPower rift = p.getPower(FrostCapRiftPower.POWER_ID);
        if (rift != null && !DominicCombatState.frostCapRiftTriggeredThisTurn) {
            DominicCombatState.frostCapRiftTriggeredThisTurn = true;
            rift.flash();
            atb(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(rift.amount, true),
                    DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
    }

    public static void onStarMarkConsumed(AbstractPlayer p) {
        if (p == null) {
            return;
        }
        // 星痕基础回馈：每层被消耗的星痕给 1 点格挡，让"铺星痕→被打消耗→收益"闭环可见（非资源条）。
        atb(new GainBlockAction(p, p, 1));
        AbstractPower sense = p.getPower(StarMarkSensePower.POWER_ID);
        if (sense != null) {
            sense.flash();
            atb(new ApplyPowerAction(p, p, new StrengthPower(p, sense.amount), sense.amount));
            atb(new ApplyPowerAction(p, p, new LoseStrengthPower(p, sense.amount), sense.amount));
        }
    }

    public static void onCardDiscarded(AbstractCard card) {
        if (card == null || AbstractDungeon.player == null) {
            return;
        }
        AbstractPlayer p = AbstractDungeon.player;
        if (DominicCombatState.abandonedQuillActive
                && card instanceof yourmod.cards.dominic.AbstractDominicCard) {
            yourmod.cards.dominic.AbstractDominicCard dc = (yourmod.cards.dominic.AbstractDominicCard) card;
            if (dc.frostPayCost > 0 && DominicCombatState.abandonedQuillFrostGained < 3) {
                DominicCombatState.abandonedQuillFrostGained++;
                FrostColdPower.gainFrost(p, 1);
            }
        }
    }

    /** 观测停笔：下回合开始结算延迟的霜偿代价 */
    public static void processObservationHalt(AbstractPlayer p) {
        if (p == null || DominicCombatState.observationHaltPendingType == null) {
            return;
        }
        FrostPayType type = DominicCombatState.observationHaltPendingType;
        int value = DominicCombatState.observationHaltPendingValue;
        DominicCombatState.observationHaltPendingType = null;
        DominicCombatState.observationHaltPendingValue = 0;
        FrostPayGateAction.applyFrostPayPenalty(p, type, value);
    }

    public static void onCardExhausted(AbstractCard card) {
        if (card != null) {
            DominicCombatState.exhaustedThisTurn.add(card);
        }
        if (AbstractDungeon.player == null) {
            return;
        }
        AbstractPower form = AbstractDungeon.player.getPower(RefinedAshFormPower.POWER_ID);
        if (form instanceof RefinedAshFormPower) {
            ((RefinedAshFormPower) form).onCardExhausted();
        }
    }

    /** 债册：回合开始抽牌 */
    public static void processDebtLedgerDraw(AbstractPlayer p) {
        if (p == null || DominicCombatState.debtLedgerDrawNextTurn <= 0) {
            return;
        }
        int n = DominicCombatState.debtLedgerDrawNextTurn;
        DominicCombatState.debtLedgerDrawNextTurn = 0;
        atb(new DrawCardAction(p, n));
    }
}
