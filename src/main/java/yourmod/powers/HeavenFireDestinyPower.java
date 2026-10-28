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

/** 天火·命定：回合结束对全体造成 8+2*烬火敌+静电敌 伤害 */
public class HeavenFireDestinyPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("HeavenFireDestiny");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public HeavenFireDestinyPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) {
            return;
        }
        flash();
        int emberEnemies = 0;
        int staticEnemies = 0;
        for (AbstractMonster m : getEnemies()) {
            if (m.hasPower(EmberFirePower.POWER_ID)) {
                emberEnemies++;
            }
            if (m.hasPower(StaticMarkPower.POWER_ID)) {
                staticEnemies++;
            }
        }
        int dmg = 8 + 2 * emberEnemies + staticEnemies;
        for (AbstractMonster m : getEnemies()) {
            addToBot(new DamageAction(m, new DamageInfo(owner, dmg, DamageInfo.DamageType.THORNS),
                    AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
