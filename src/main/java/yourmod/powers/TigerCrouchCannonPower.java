package yourmod.powers;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 虎蹲炮：倒计时结束后直接击晕持有者（与磁吸炸弹同一引爆时机） */
public class TigerCrouchCannonPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("TigerCrouchCannon");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public TigerCrouchCannonPower(AbstractCreature owner, int turns, int stunTurns) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, true, owner, turns);
        this.isTwoAmount = true;
        this.amount2 = stunTurns;
    }

    /** 再次挂上时叠加晕眩回合，倒计时取较短者 */
    public void stackCannon(int turns, int stunTurns) {
        this.fontScale = 8.0F;
        this.amount2 += stunTurns;
        if (turns < this.amount) {
            this.amount = turns;
        }
        updateDescription();
    }

    @Override
    public void atEndOfRound() {
        if (amount <= 1) {
            flash();
            if (owner instanceof AbstractMonster && !owner.isDeadOrEscaped()) {
                addToBot(new StunMonsterAction((AbstractMonster) owner, AbstractDungeon.player, amount2));
            }
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
