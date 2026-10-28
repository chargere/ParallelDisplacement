package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.powers.AbstractEasyPower;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

/** 精绝王者：说明处决触发条件，无实际效果。 */
public class ExecuteForcePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjExecuteForce");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ExecuteForcePower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
        useVanillaIcon("ritual");
    }

    @Override
    public void stackPower(int stackAmount) {
    }

    @Override
    public void reducePower(int reduceAmount) {
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        JingjueCombatState.syncErosionPowers();
    }

    @Override
    public void updateDescription() {
        locDesc(strings.DESCRIPTIONS[0]);
    }
}
