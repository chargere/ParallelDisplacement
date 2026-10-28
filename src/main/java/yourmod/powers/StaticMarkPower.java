package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/**
 * 静电印记：挂在敌人身上的负面。
 * 传导逻辑在 ScorchingAssaultPower.onAttack 里处理。
 */
public class StaticMarkPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("StaticMark");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public StaticMarkPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, false, owner, amount);
    }

    @Override
    public void onInitialApplication() {
        if (owner instanceof com.megacrit.cardcrawl.monsters.AbstractMonster) {
            yourmod.util.HongwenHooks.onStaticApplied((com.megacrit.cardcrawl.monsters.AbstractMonster) owner, amount);
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (owner instanceof com.megacrit.cardcrawl.monsters.AbstractMonster) {
            yourmod.util.HongwenHooks.onStaticApplied((com.megacrit.cardcrawl.monsters.AbstractMonster) owner, stackAmount);
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
