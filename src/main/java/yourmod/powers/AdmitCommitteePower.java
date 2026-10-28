package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static yourmod.ModFile.makeID;

/** 鎵胯濮斿憳 */
public class AdmitCommitteePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("AdmitCommittee");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private int timesThisTurn = 0;

    public AdmitCommitteePower(AbstractCreature owner, int maxPerTurn) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, maxPerTurn);
    }

    public void onUselessGained(int am) {
        if (am <= 0 || timesThisTurn >= amount) {
            return;
        }
        timesThisTurn++;
        flash();
        addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, 1), 1));
    }

    @Override
    public void atStartOfTurn() {
        timesThisTurn = 0;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}