package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberFirePower;
import yourmod.powers.StaticMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.pwrAmt;

/**
 * 共振（1费）
 * 升级前：4 伤；伤害+目标烬火与静电层数之和
 * 升级后：7 伤（缩放公式不变）
 */
public class Resonance extends AbstractEasyCard {
    public final static String ID = makeID("Resonance");

    public Resonance() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 4;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBase = baseDamage;
        if (mo != null) {
            baseDamage += pwrAmt(mo, EmberFirePower.POWER_ID) + pwrAmt(mo, StaticMarkPower.POWER_ID);
        }
        super.calculateCardDamage(mo);
        baseDamage = realBase;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.LIGHTNING);
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
