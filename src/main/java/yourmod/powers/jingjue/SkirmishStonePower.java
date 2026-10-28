package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.powers.AbstractEasyPower;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

public class SkirmishStonePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjSkirmishStone");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public SkirmishStonePower(AbstractCreature owner, int capBonus) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, capBonus);
        JingjueCombatState.hourglassCapBonus += capBonus;
        useVanillaIcon("focus");
    }

    @Override
    public void updateDescription() {
        locDesc(strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1]);
    }
}
