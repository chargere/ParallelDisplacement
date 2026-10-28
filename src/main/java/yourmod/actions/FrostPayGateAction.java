package yourmod.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.EasyModalChoiceCard;
import yourmod.cards.dominic.AbstractDominicCard;
import yourmod.cards.dominic.FrostPayType;
import yourmod.powers.DelayTaxPower;
import yourmod.powers.FreezeCrackPower;
import yourmod.powers.FrostColdPower;
import yourmod.powers.ReturnToVoidPower;
import yourmod.powers.StarExtinguishPower;
import yourmod.relics.ObservationHalt;
import yourmod.util.DominicCombatState;
import yourmod.util.DominicHooks;

import java.util.ArrayList;

/**
 * 霜偿结算：Shift 直接耗霜寒取消；霜寒足够则弹窗二选一；否则承受代价。
 * 使用 EasyModalChoiceAction，避免 BaseMod ModalChoice 二次出牌导致手牌锁死。
 */
public class FrostPayGateAction extends AbstractGameAction {
    private final AbstractDominicCard card;
    private final AbstractPlayer player;
    private final AbstractMonster target;
    private final Runnable effect;

    public FrostPayGateAction(AbstractDominicCard card, AbstractPlayer player, AbstractMonster target, Runnable effect) {
        this.card = card;
        this.player = player;
        this.target = target;
        this.effect = effect;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
    }

    @Override
    public void update() {
        DominicCombatState.lostDexBeforeFrostPay = DominicCombatState.lostDexThisTurn;

        if (card.frostPayCost <= 0 || card.frostPayType == null) {
            onCardPlayed();
            effect.run();
            isDone = true;
            return;
        }

        int frost = FrostColdPower.getFrost(player);
        boolean shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
                || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);

        if (shift && frost >= card.frostPayCost) {
            cancelPay();
            onCardPlayed();
            effect.run();
            isDone = true;
            return;
        }

        if (frost < card.frostPayCost) {
            acceptPay();
            onCardPlayed();
            effect.run();
            isDone = true;
            return;
        }

        int shownCost = previewCancelCost();
        String penaltyName = card.frostPayType.displayName;
        String penaltyDetail = card.frostPayType.detail(card.frostPayValue);
        ArrayList<AbstractCard> choices = new ArrayList<>();
        choices.add(new EasyModalChoiceCard(
                "承受：" + penaltyName,
                "打出此牌，并承受霜偿——" + penaltyDetail,
                () -> {
                    acceptPay();
                    onCardPlayed();
                    effect.run();
                }));
        choices.add(new EasyModalChoiceCard(
                "取消代价",
                "消耗 " + shownCost + " 层霜寒，取消本次霜偿（不触发「" + penaltyName + "」）。",
                () -> {
                    cancelPay();
                    onCardPlayed();
                    effect.run();
                }));
        AbstractDungeon.actionManager.addToTop(new EasyModalChoiceAction(choices, 1, "霜偿"));
        isDone = true;
    }

    private void onCardPlayed() {
        if (card.frostPayCost > 0) {
            DominicCombatState.frostPayCardsPlayedThisTurn++;
        }
    }

    private int previewCancelCost() {
        int cost = card.frostPayCost;
        int discount = 0;
        if (player.getPower(yourmod.powers.FrostPayDiscountPower.POWER_ID) != null
                && !DominicCombatState.frostPayDiscountUsedThisTurn) {
            discount += 1;
        }
        if (DominicCombatState.auroraCommandDiscountsLeft > 0) {
            discount += 1;
        }
        if (player.hasRelic(yourmod.relics.MirrorPocketWatch.ID)
                && DominicCombatState.mirrorWatchCancelAvailableThisTurn) {
            discount += 1;
        }
        if (discount > 0) {
            return Math.max(1, cost - discount);
        }
        return cost;
    }

    private void cancelPay() {
        int cost = DominicHooks.modifyFrostPayCancelCost(player, card.frostPayCost);
        FrostColdPower.spendFrost(player, cost);
        DominicHooks.onActiveFrostSpend(player, cost);
        DominicHooks.onFrostPayCancelled(player);
    }

    private void acceptPay() {
        DominicHooks.onFrostPayAccepted(player);
        if (tryDelayPenaltyWithObservationHalt()) {
            return;
        }
        applyFrostPayPenalty(player, card.frostPayType, card.frostPayValue);
    }

    private boolean tryDelayPenaltyWithObservationHalt() {
        if (!player.hasRelic(ObservationHalt.ID)) {
            return false;
        }
        if (DominicCombatState.observationHaltUsedThisTurn) {
            return false;
        }
        if (DominicCombatState.frostPayCancelledThisTurn > 0) {
            return false;
        }
        DominicCombatState.observationHaltPendingType = card.frostPayType;
        DominicCombatState.observationHaltPendingValue = card.frostPayValue;
        DominicCombatState.observationHaltUsedThisTurn = true;
        com.megacrit.cardcrawl.relics.AbstractRelic relic = player.getRelic(ObservationHalt.ID);
        if (relic != null) {
            relic.flash();
        }
        return true;
    }

    public static void applyFrostPayPenalty(AbstractPlayer player, FrostPayType type, int value) {
        if (player == null || type == null) {
            return;
        }
        switch (type) {
            case DELAY:
                DelayTaxPower.applyToNeighbors(Math.max(1, value));
                break;
            case FREEZE_CRACK:
                FreezeCrackPower.apply(player, value);
                break;
            case STAR_EXTINGUISH:
                AbstractDungeon.actionManager.addToBottom(
                        new ApplyPowerAction(player, player, new StarExtinguishPower(player), 1));
                break;
            case RETURN_TO_VOID:
                AbstractDungeon.actionManager.addToBottom(
                        new ApplyPowerAction(player, player, new ReturnToVoidPower(player, value), value));
                break;
            default:
                break;
        }
    }
}
