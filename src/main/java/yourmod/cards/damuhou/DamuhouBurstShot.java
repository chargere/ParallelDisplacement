package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** A4 连续射击 */
public class DamuhouBurstShot extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouBurstShot");

    public DamuhouBurstShot() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 2;
        baseMagicNumber = magicNumber = 3;
        printedCombo = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        comboHits(m, magicNumber, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

    @Override
    public void upp() {
        upgradeDamage(1);
    }
}
