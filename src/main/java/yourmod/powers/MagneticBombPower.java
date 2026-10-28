package yourmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 磁吸炸弹：倒计时结束后对持有者造成固定伤害 */
public class MagneticBombPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("MagneticBomb");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public MagneticBombPower(AbstractCreature owner, int turns, int damage) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, true, owner, turns);
        this.isTwoAmount = true;
        this.amount2 = damage;
    }

    /** 再次挂上时叠加伤害，倒计时取较短者 */
    public void stackBomb(int turns, int damage) {
        this.fontScale = 8.0F;
        this.amount2 += damage;
        if (turns < this.amount) {
            this.amount = turns;
        }
        updateDescription();
    }

    @Override
    public void atEndOfRound() {
        if (amount <= 1) {
            flash();
            addToBot(new DamageAction(owner,
                    new DamageInfo(AbstractDungeon.player, amount2, DamageInfo.DamageType.THORNS),
                    AbstractGameAction.AttackEffect.FIRE));
            addToBot(new RemoveSpecificPowerAction(owner, owner, this.ID));
        } else {
            addToBot(new ReducePowerAction(owner, owner, this.ID, 1));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1]
                + amount2 + strings.DESCRIPTIONS[2];
    }
}
