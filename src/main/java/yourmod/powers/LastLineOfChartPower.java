package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;

/** 星图最后一行：归墟改为进弃牌堆，每场最多 amount 次 */
public class LastLineOfChartPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("LastLineOfChart");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public LastLineOfChartPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        DominicCombatState.lastLineChartRemaining = amount;
    }

    public void consumeUse() {
        amount--;
        DominicCombatState.lastLineChartRemaining = amount;
        updateDescription();
        if (amount <= 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
