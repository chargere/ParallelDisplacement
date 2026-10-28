package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/**
 * 星痕：下一次受到的攻击伤害 +4，每层独立触发并消耗。
 */
public class StarMarkPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("StarMark");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int BONUS = 4;

    public StarMarkPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, false, owner, amount);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL && amount > 0) {
            return damage + BONUS;
        }
        return damage;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL && info.owner != null && info.owner != this.owner && amount > 0) {
            flash();
            addToTop(new ReducePowerAction(owner, owner, this, 1));
            if (info.owner instanceof com.megacrit.cardcrawl.characters.AbstractPlayer) {
                yourmod.util.DominicHooks.onStarMarkConsumed(
                        (com.megacrit.cardcrawl.characters.AbstractPlayer) info.owner);
            }
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + BONUS + strings.DESCRIPTIONS[1] + amount + strings.DESCRIPTIONS[2];
    }
}
