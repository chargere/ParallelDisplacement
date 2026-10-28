package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.powers.AbstractEasyPower;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

public class WalkingIllusionPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjWalkingIllusion");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public WalkingIllusionPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("confusion");
    }

    @Override
    public void atStartOfTurn() {
        flash();
        Wiz.forAllMonstersLiving(m -> addToBot(new ApplyPowerAction(m, owner, new ImaginaryPower(m, amount), amount)));
    }

    @Override
    public void updateDescription() {
        locDesc(strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1]);
    }
}
