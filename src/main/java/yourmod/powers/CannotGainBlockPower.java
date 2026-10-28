package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 无法获得格挡：持续若干回合，获得的格挡变为 0 */
public class CannotGainBlockPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("CannotGainBlock");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public CannotGainBlockPower(AbstractCreature owner, int turns) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, true, owner, turns);
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return 0;
    }

    @Override
    public void atEndOfRound() {
        if (amount <= 0) {
            addToBot(new ReducePowerAction(owner, owner, this.ID, 999));
            return;
        }
        addToBot(new ReducePowerAction(owner, owner, this.ID, 1));
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = strings.DESCRIPTIONS[0];
        } else {
            description = strings.DESCRIPTIONS[1] + amount + strings.DESCRIPTIONS[2];
        }
    }
}
