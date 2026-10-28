package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

public class DamuhouLeftoverPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("DamuhouLeftover");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DamuhouLeftoverPower(AbstractCreature owner, int blockPerMiss, int overflowBreach) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, blockPerMiss);
        isTwoAmount = true;
        amount2 = overflowBreach;
        useVanillaIcon("pen_nib");
    }

    public int overflowBreach() {
        return Math.max(0, amount2);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1]
                + overflowBreach() + strings.DESCRIPTIONS[2];
    }
}
