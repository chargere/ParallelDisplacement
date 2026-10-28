package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.ThunderScarMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToEnemy;

/**
 * 雷痕（1费）
 * 升级前：5 伤；施加雷痕；消耗
 * 升级后：8 伤
 */
public class ThunderScar extends AbstractEasyCard {
    public final static String ID = makeID("ThunderScar");

    public ThunderScar() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 5;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.LIGHTNING);
        applyToEnemy(m, new ThunderScarMarkPower(m));
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
