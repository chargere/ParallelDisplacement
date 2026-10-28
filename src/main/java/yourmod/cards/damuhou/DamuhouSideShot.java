package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** A6 侧射 */
public class DamuhouSideShot extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouSideShot");

    public DamuhouSideShot() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 4;
        baseMagicNumber = magicNumber = 1;
        printedCombo = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int hits = 2;
        if (targetIsMiddle(m)) {
            hits += magicNumber;
        }
        comboHits(m, hits, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
