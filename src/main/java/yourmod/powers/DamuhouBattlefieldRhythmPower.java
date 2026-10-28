package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

public class DamuhouBattlefieldRhythmPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("DamuhouBattlefieldRhythm");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DamuhouBattlefieldRhythmPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("equilibrium");
    }

    @Override
    public void onInitialApplication() {
        DamuhouCombatState.setNthCardDiscount(amount);
    }

    @Override
    public void stackPower(int stackAmount) {
        fontScale = 8.0F;
        this.amount = Math.min(this.amount, stackAmount);
        updateDescription();
        DamuhouCombatState.setNthCardDiscount(amount);
    }

    @Override
    public void atStartOfTurn() {
        DamuhouCombatState.setNthCardDiscount(amount);
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
