package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 琚姩鏀诲嚮 */
public class PassiveAggressionPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("PassiveAggression");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private int timesThisTurn = 0;

    public PassiveAggressionPower(AbstractCreature owner, int maxPerTurn) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, maxPerTurn);
    }

    public void onDiscard() {
        if (timesThisTurn >= amount) {
            return;
        }
        if (AbstractDungeon.cardRandomRng.randomBoolean()) {
            timesThisTurn++;
            flash();
            UselessPower.gain(owner, owner, 1);
        }
    }

    @Override
    public void atStartOfTurn() {
        timesThisTurn = 0;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}