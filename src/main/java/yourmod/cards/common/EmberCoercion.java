package yourmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberFirePower;

import static yourmod.ModFile.makeID;

/**
 * 烬火胁迫（0费）
 * 升级前：4 伤；1 虚弱；有烬火再 1 易伤
 * 升级后：6 伤；2 虚弱
 */
public class EmberCoercion extends AbstractEasyCard {
    public final static String ID = makeID("EmberCoercion");

    public EmberCoercion() {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 4;
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.FIRE);
        addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
        if (m.hasPower(EmberFirePower.POWER_ID)) {
            addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, 1, false), 1));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
    }
}
