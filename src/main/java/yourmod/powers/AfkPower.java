package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.util.FvvCombatState;

import static yourmod.ModFile.makeID;

/** 鎸傛満 */
public class AfkPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("Afk");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    /** 姣忓洖鍚堟墦鐗屼笂闄愶紙鍚級 */
    public int playLimit;

    public AfkPower(AbstractCreature owner, int playLimit) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, 1);
        this.playLimit = playLimit;
        this.isTwoAmount = true;
        this.amount2 = playLimit;
    }

    @Override
    public void stackPower(int stackAmount) {
        // keep highest playLimit when stacking
    }

    @Override
    public void atStartOfTurn() {
        if (FvvCombatState.lowPlayStreak >= 2) {
            flash();
            addToBot(new GainEnergyAction(2));
            UselessPower.gain(owner, owner, 3);
            FvvCombatState.lowPlayStreak = 0;
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + playLimit + strings.DESCRIPTIONS[1];
    }
}