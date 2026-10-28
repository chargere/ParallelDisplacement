package yourmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.CannotGainBlockPower;

import static yourmod.ModFile.makeID;

/**
 * 雷霆震慑（2费→升级1费）
 * 升级前：10 伤；若虚弱则耗1虚弱并使 1 回合无法格挡
 * 升级后：费用1；无法格挡持续 2 回合
 */
public class ThunderShock extends AbstractEasyCard {
    public final static String ID = makeID("ThunderShock");

    public ThunderShock() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 10;
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.LIGHTNING);
        if (m.hasPower(WeakPower.POWER_ID)) {
            addToBot(new ReducePowerAction(m, p, WeakPower.POWER_ID, 1));
            addToBot(new ApplyPowerAction(m, p, new CannotGainBlockPower(m, magicNumber), magicNumber));
        }
    }

    @Override
    public void upp() {
        upgradeBaseCost(1);
        upgradeMagicNumber(1);
    }
}
