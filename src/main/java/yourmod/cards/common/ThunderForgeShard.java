package yourmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberFirePower;
import yourmod.powers.StaticMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToEnemy;

/** 雷铸残片：伤；有静电则加伤+烬火；有烬火则消耗。 */
public class ThunderForgeShard extends AbstractEasyCard {
    public final static String ID = makeID("ThunderForgeShard");

    public ThunderForgeShard() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 7;
        baseSecondDamage = 4;
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        boolean hasStatic = m != null && m.hasPower(StaticMarkPower.POWER_ID);
        boolean hasEmber = m != null && m.hasPower(EmberFirePower.POWER_ID);
        if (hasStatic) {
            altDmg(m, AbstractGameAction.AttackEffect.LIGHTNING);
            applyToEnemy(m, new EmberFirePower(m, magicNumber));
        }
        if (hasEmber) {
            exhaustOnUseOnce = true;
        }
    }

    @Override
    public void upp() {
        upgradeDamage(3);
        upgradeSecondDamage(2);
    }
}
