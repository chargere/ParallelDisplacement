package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

public class DamuhouFocusPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("DamuhouFocus");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DamuhouFocusPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("focus");
    }

    @Override
    public void onInitialApplication() {
        applyStanding();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        DamuhouCombatState.firstAttackCombo += stackAmount;
        DamuhouCombatState.firstAttackKillEnergy += 1;
    }

    @Override
    public void atStartOfTurn() {
        applyStanding();
    }

    private void applyStanding() {
        DamuhouCombatState.firstAttackCombo += amount;
        DamuhouCombatState.firstAttackKillEnergy += 1;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
