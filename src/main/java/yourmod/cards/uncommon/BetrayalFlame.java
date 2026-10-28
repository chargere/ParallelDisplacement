package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberFirePower;
import yourmod.powers.StaticMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToEnemy;
import static yourmod.util.Wiz.getRandomEnemy;

/**
 * 背叛之焰（0费）
 * 升级前：5 伤；被消耗时随机敌人 6 烬火与 4 静电
 * 升级后：7 伤；被消耗时 8 烬火（静电仍4）
 */
public class BetrayalFlame extends AbstractEasyCard {
    public final static String ID = makeID("BetrayalFlame");

    public BetrayalFlame() {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 5;
        baseMagicNumber = magicNumber = 6;
        baseSecondMagic = secondMagic = 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.FIRE);
    }

    @Override
    public void triggerOnExhaust() {
        AbstractMonster m = getRandomEnemy();
        if (m != null) {
            applyToEnemy(m, new EmberFirePower(m, magicNumber));
            applyToEnemy(m, new StaticMarkPower(m, secondMagic));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(2);
    }
}
