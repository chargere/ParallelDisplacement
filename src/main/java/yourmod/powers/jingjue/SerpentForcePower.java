package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.powers.AbstractEasyPower;

import static yourmod.ModFile.makeID;

public class SerpentForcePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjSerpentForce");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public SerpentForcePower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("infiniteblades");
    }

    @Override
    public void updateDescription() {
        locDesc(strings.DESCRIPTIONS[0]);
    }
}
