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
 * 焚灼痛击（1费）
 * 升级前：6 伤；2 易伤；有烬火再 1 虚弱
 * 升级后：8 伤；3 易伤
 */
public class BurnPainStrike extends AbstractEasyCard {
    public final static String ID = makeID("BurnPainStrike");

    public BurnPainStrike() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.FIRE);
        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber));
        if (m.hasPower(EmberFirePower.POWER_ID)) {
            addToBot(new ApplyPowerAction(m, p, new WeakPower(m, 1, false), 1));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
    }
}
