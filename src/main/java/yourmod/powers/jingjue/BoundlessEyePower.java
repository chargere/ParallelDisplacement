package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.powers.AbstractEasyPower;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

public class BoundlessEyePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjBoundlessEye");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public BoundlessEyePower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("curiosity");
    }

    @Override
    public void atStartOfTurn() {
        flash();
        Wiz.forAllMonstersLiving(m -> {
            int have = m.hasPower(ImaginaryPower.POWER_ID) ? m.getPower(ImaginaryPower.POWER_ID).amount : 0;
            if (have < 3) {
                addToBot(new ApplyPowerAction(m, owner, new ImaginaryPower(m, 1), 1));
            }
        });
    }

    @Override
    public void updateDescription() {
        locDesc(strings.DESCRIPTIONS[0]);
    }
}
