package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

public class DamuhouCalmAimPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("DamuhouCalmAim");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DamuhouCalmAimPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("blur");
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (DamuhouCombatState.attacksPlayedThisTurn <= 0) {
            flash();
            addToBot(new GainBlockAction(owner, owner, amount));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
