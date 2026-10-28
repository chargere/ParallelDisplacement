package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.powers.AbstractEasyPower;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

public class ZagrammaEyePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjZagrammaEye");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ZagrammaEyePower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, 1);
        JingjueCombatState.eyeMarkNoDecay = true;
        JingjueCombatState.eyeMarkPctOverride = 7;
        useVanillaIcon("accuracy");
    }

    @Override
    public void updateDescription() {
        locDesc(strings.DESCRIPTIONS[0]);
    }
}
