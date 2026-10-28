package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.powers.AbstractEasyPower;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

/** 沙漏 */
public class HourglassPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjHourglass");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public HourglassPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("timewarp");
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount > JingjueCombatState.hourglassCap()) {
            amount = JingjueCombatState.hourglassCap();
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        locDesc(strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1]
                + JingjueCombatState.hourglassCap() + strings.DESCRIPTIONS[2]);
    }
}
