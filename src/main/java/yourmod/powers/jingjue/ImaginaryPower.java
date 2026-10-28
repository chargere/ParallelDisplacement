package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.AbstractEasyPower;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

/** 虚数：本回合跳过普通怪意图；精英/Boss 减伤；受伤 +25%。回合结束全部消散。 */
public class ImaginaryPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjImaginary");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public boolean cancelled;

    public ImaginaryPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, true, owner, amount);
        useVanillaIcon("wraith");
        cancelled = false;
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (owner instanceof AbstractMonster) {
            int cap = JingjueCombatState.imaginaryCap((AbstractMonster) owner);
            if (amount > cap) {
                amount = cap;
            }
        }
        updateDescription();
    }

    public void clearStored() {
        cancelled = true;
        TimeTurbulencePower.clear(owner);
        updateDescription();
    }

    @Override
    public void atEndOfRound() {
        if (!cancelled && owner instanceof AbstractMonster
                && !JingjueCombatState.blocksTimeTurbulence((AbstractMonster) owner)) {
            int stored = JingjueCombatState.takeCapturedIntent((AbstractMonster) owner);
            if (stored > 0) {
                int delay = Math.max(0, amount - 1);
                addToBot(new ApplyPowerAction(owner, owner,
                        new TimeTurbulencePower(owner, stored, delay), stored));
            }
        }
        addToBot(new RemoveSpecificPowerAction(owner, owner, ID));
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL && amount > 0) {
            return damage * (1f + JingjueCombatState.IMAGINARY_INCOMING);
        }
        return damage;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (owner instanceof AbstractMonster && JingjueCombatState.isEliteOrBoss((AbstractMonster) owner)
                && type == DamageInfo.DamageType.NORMAL && amount > 0) {
            float cut = Math.min(JingjueCombatState.IMAGINARY_OUTGOING_CAP,
                    JingjueCombatState.IMAGINARY_OUTGOING * amount);
            return damage * (1f - cut);
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        boolean elite = owner instanceof AbstractMonster
                && JingjueCombatState.isEliteOrBoss((AbstractMonster) owner);
        locDesc(strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[elite ? 2 : 1]);
    }
}
