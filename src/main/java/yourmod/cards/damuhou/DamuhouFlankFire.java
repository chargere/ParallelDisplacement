package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

/** 侧翼夹击 */
public class DamuhouFlankFire extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouFlankFire");

    public DamuhouFlankFire() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 3;
        baseSecondDamage = secondDamage = 2;
        baseMagicNumber = magicNumber = 2;
        printedCombo = 2;
        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        comboHits(m, 2, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        if (Wiz.getEnemies().size() >= 3) {
            int saved = damage;
            damage = secondDamage;
            calculateCardDamage(null);
            for (int i = 0; i < magicNumber; i++) {
                allDmg(AbstractGameAction.AttackEffect.FIRE);
            }
            damage = saved;
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
