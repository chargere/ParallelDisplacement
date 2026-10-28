package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.getEnemies;

/** 蓄雷姿态：下回合开始时对所有敌人施加静电印记，然后移除自身 */
public class ChargeStancePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("ChargeStance");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ChargeStancePower(AbstractCreature owner, int marks) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, marks);
    }

    @Override
    public void atStartOfTurn() {
        flash();
        for (AbstractMonster m : getEnemies()) {
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(m, owner, new StaticMarkPower(m, amount), amount));
        }
        addToBot(new RemoveSpecificPowerAction(owner, owner, this.ID));
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
