package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberFirePower;
import yourmod.powers.StaticMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.pwrAmt;

/**
 * 焚雷换位（1费）
 * 升级前：6 伤；交换目标烬火与静电层数
 * 升级后：9 伤
 */
public class EmberThunderSwap extends AbstractEasyCard {
    public final static String ID = makeID("EmberThunderSwap");

    public EmberThunderSwap() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 6;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.FIRE);
        int ember = pwrAmt(m, EmberFirePower.POWER_ID);
        int staticAmt = pwrAmt(m, StaticMarkPower.POWER_ID);
        if (ember == staticAmt) {
            return;
        }
        AbstractPower ep = m.getPower(EmberFirePower.POWER_ID);
        AbstractPower sp = m.getPower(StaticMarkPower.POWER_ID);
        if (ep != null) {
            addToBot(new ReducePowerAction(m, p, EmberFirePower.POWER_ID, ember));
        }
        if (sp != null) {
            addToBot(new ReducePowerAction(m, p, StaticMarkPower.POWER_ID, staticAmt));
        }
        if (staticAmt > 0) {
            addToBot(new ApplyPowerAction(m, p, new EmberFirePower(m, staticAmt), staticAmt));
        }
        if (ember > 0) {
            addToBot(new ApplyPowerAction(m, p, new StaticMarkPower(m, ember), ember));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
