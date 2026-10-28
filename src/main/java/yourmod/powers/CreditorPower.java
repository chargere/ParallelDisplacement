package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static yourmod.ModFile.makeID;

/** 债主：承受霜偿得临时力量，取消霜偿得格挡 */
public class CreditorPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("Creditor");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public CreditorPower(AbstractCreature owner, int strength, int block) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, strength);
        this.amount2 = block;
        this.isTwoAmount = true;
        updateDescription();
    }

    public void onAccept() {
        flash();
        addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount), amount));
        addToBot(new ApplyPowerAction(owner, owner, new LoseStrengthPower(owner, amount), amount));
    }

    public void onCancel() {
        flash();
        addToBot(new GainBlockAction(owner, owner, amount2));
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1]
                + amount2 + strings.DESCRIPTIONS[2];
    }
}
