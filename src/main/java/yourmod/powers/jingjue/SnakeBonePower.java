package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.powers.AbstractEasyPower;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

public class SnakeBonePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjSnakeBone");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public int percent;

    public SnakeBonePower(AbstractCreature owner, int percent) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, percent);
        this.percent = percent;
        JingjueCombatState.splashTriggersExecute = true;
        useVanillaIcon("sharp_hide");
    }

    @Override
    public void updateDescription() {
        locDesc(strings.DESCRIPTIONS[0] + percent + strings.DESCRIPTIONS[1]);
    }
}
