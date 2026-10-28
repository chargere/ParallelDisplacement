package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;

/** 薄霜纪律：回合结束时若本回合未获霜寒则获得格挡 */
public class ThinFrostDisciplinePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("ThinFrostDiscipline");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ThinFrostDisciplinePower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) {
            return;
        }
        if (DominicCombatState.frostGainedThisTurn == 0) {
            flash();
            addToBot(new GainBlockAction(owner, owner, amount));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
