package yourmod.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.DominicCharacter;
import yourmod.powers.FrostColdPower;
import yourmod.util.DominicCombatState;
import yourmod.util.DominicHooks;

import static yourmod.ModFile.makeID;

/**
 * 极光怀表：开战 4 霜寒；每回合第一张牌获得与费用相同的霜寒。
 */
public class AuroraPocketWatch extends AbstractEasyRelic {
    public static final String ID = makeID("AuroraPocketWatch");
    private static final int START_FROST = 4;
    private boolean firstCardThisTurn = true;

    public AuroraPocketWatch() {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL, DominicCharacter.Enums.DOMINIC_COLOR);
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
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AuroraPocketWatch();
    }
}
