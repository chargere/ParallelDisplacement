package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.powers.AbstractEasyPower;

import static yourmod.ModFile.makeID;

public class CorridorPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjCorridor");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public CorridorPower(AbstractCreature owner, int maxPerTurn) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, maxPerTurn);
        useVanillaIcon("equilibrium");
    }

    @Override
    public void updateDescription() {
        locDesc(strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1]);
    }
}
