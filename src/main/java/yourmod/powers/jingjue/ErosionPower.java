package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.AbstractEasyPower;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

/** 侵蚀度：展示当前处决线百分比，无额外效果。 */
public class ErosionPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjErosion");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ErosionPower(AbstractCreature owner, int pct) {
        super(POWER_ID, strings.NAME, owner instanceof AbstractMonster ? PowerType.DEBUFF : PowerType.BUFF,
                false, owner, Math.max(1, pct));
        useVanillaIcon("accuracy");
    }

    public void setPct(int pct) {
        int next = Math.max(1, pct);
        if (amount == next) {
            return;
        }
        amount = next;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        setPct(JingjueCombatState.executeLinePct(owner instanceof AbstractMonster ? (AbstractMonster) owner : null));
    }

    @Override
    public void reducePower(int reduceAmount) {
    }

    @Override
    public void updateDescription() {
        locDesc(strings.DESCRIPTIONS[0] + amount + (owner != null && owner.isPlayer
                ? strings.DESCRIPTIONS[1]
                : strings.DESCRIPTIONS[2]));
    }
}
