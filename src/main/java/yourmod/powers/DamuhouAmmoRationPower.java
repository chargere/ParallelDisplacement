package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

public class DamuhouAmmoRationPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("DamuhouAmmoRation");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DamuhouAmmoRationPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("brutality");
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        DamuhouCombatState.ammoRationActive = false;
        addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
