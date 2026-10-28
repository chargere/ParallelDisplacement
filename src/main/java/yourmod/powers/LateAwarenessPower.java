package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.util.FvvCombatState;

import static yourmod.ModFile.makeID;

/** 杩熷埌瑙夋偀 */
public class LateAwarenessPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("LateAwareness");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public LateAwarenessPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && FvvCombatState.damageDealtThisTurn == 0) {
            flash();
            UselessPower.gain(owner, owner, amount);
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}