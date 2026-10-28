package yourmod.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.DominicCharacter;
import yourmod.cards.dominic.AbstractDominicCard;
import yourmod.powers.FrostColdPower;
import yourmod.util.DominicCombatState;
import yourmod.util.DominicHooks;

import static yourmod.ModFile.makeID;

/**
 * 倒影怀表（Boss）：替换极光怀表。
 * 开战 +4 霜寒；每回合首张牌按费用叠霜（带霜偿再 +2）；每回合一次取消耗霜 -1（最低1）。
 */
public class MirrorPocketWatch extends AbstractEasyRelic {
    public static final String ID = makeID("MirrorPocketWatch");
    private static final int START_FROST = 4;
    private static final int FROST_PAY_BONUS = 2;
    private boolean firstCardThisTurn = true;

    public MirrorPocketWatch() {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL, DominicCharacter.Enums.DOMINIC_COLOR);
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player != null
                && AbstractDungeon.player.hasRelic(AuroraPocketWatch.ID);
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(AuroraPocketWatch.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(AuroraPocketWatch.ID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                    return;
                }
            }
        }
        super.obtain();
    }

    @Override
    public void bossObtainLogic() {
        obtain();
        isObtained = true;
    }

    @Override
    public void atBattleStart() {
        DominicCombatState.onBattleStart();
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        FrostColdPower.gainFrostImmediate(AbstractDungeon.player, START_FROST);
        firstCardThisTurn = true;
        beginLongPulse();
    }

    @Override
    public void atTurnStart() {
        DominicHooks.processObservationHalt(AbstractDungeon.player);
        DominicHooks.processDebtLedgerDraw(AbstractDungeon.player);
        DominicCombatState.onTurnStart();
        DominicCombatState.mirrorWatchCancelAvailableThisTurn = true;
        firstCardThisTurn = true;
        beginLongPulse();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!firstCardThisTurn) {
            return;
        }
        firstCardThisTurn = false;
        stopPulse();
        int cost = card.costForTurn;
        if (card.costForTurn == -1) {
            cost = card.energyOnUse;
        }
        if (cost < 0) {
            cost = 0;
        }
        if (cost > 0) {
            flash();
            FrostColdPower.gainFrost(AbstractDungeon.player, cost);
        }
        if (card instanceof AbstractDominicCard) {
            AbstractDominicCard dc = (AbstractDominicCard) card;
            if (dc.frostPayCost > 0) {
                flash();
                FrostColdPower.gainFrost(AbstractDungeon.player, FROST_PAY_BONUS);
            }
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MirrorPocketWatch();
    }
}
