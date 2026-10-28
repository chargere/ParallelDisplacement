package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;

/** 极光帷幄：设置本回合霜偿取消折扣次数（实际折扣在 DominicHooks） */
public class AuroraCommandPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("AuroraCommand");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public AuroraCommandPower(AbstractCreature owner, int discounts) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, true, owner, discounts);
        DominicCombatState.auroraCommandDiscountsLeft = Math.max(
                DominicCombatState.auroraCommandDiscountsLeft, discounts);
    }

    @Override
    public void onInitialApplication() {
        DominicCombatState.auroraCommandDiscountsLeft = Math.max(
                DominicCombatState.auroraCommandDiscountsLeft, amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
