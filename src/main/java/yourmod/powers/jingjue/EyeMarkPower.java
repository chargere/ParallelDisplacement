package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.powers.AbstractEasyPower;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

/** 眼印 */
public class EyeMarkPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjEyeMark");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private int decayTimer;

    public EyeMarkPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, true, owner, amount);
        useVanillaIcon("curiosity");
        decayTimer = 0;
    }

    @Override
    public void atEndOfRound() {
        if (JingjueCombatState.eyeMarkNoDecay) {
            return;
        }
        amount--;
        if (amount <= 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, ID));
        } else {
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        locDesc(strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1]);
    }
}
