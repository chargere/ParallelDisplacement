package yourmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.getEnemies;

/** 红温·极意：无法回复；回复量×3 转为全体失去生命 */
public class RedHeatExtremePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("RedHeatExtreme");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public RedHeatExtremePower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
    }

    @Override
    public int onHeal(int healAmount) {
        if (healAmount > 0) {
            flash();
            int loss = healAmount * 3;
            for (AbstractMonster m : getEnemies()) {
                addToBot(new DamageAction(m, new DamageInfo(owner, loss, DamageInfo.DamageType.HP_LOSS),
                        AbstractGameAction.AttackEffect.FIRE));
            }
            return 0;
        }
        return healAmount;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
