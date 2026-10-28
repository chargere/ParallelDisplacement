package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 命火契约：受到的回复减半 */
public class HealHalvePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("HealHalve");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public HealHalvePower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, false, owner, -1);
    }

    @Override
    public int onHeal(int healAmount) {
        return healAmount / 2;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
