package yourmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.StaticMarkPower;

import static yourmod.ModFile.makeID;

/**
 * 雷颤（1费）
 * 升级前：7 伤；1 虚弱；有静电再 1 易伤
 * 升级后：9 伤；2 虚弱
 */
public class ThunderTremor extends AbstractEasyCard {
    public final static String ID = makeID("ThunderTremor");

    public ThunderTremor() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 7;
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.LIGHTNING);
        addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
        if (m.hasPower(StaticMarkPower.POWER_ID)) {
            addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, 1, false), 1));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
    }
}
