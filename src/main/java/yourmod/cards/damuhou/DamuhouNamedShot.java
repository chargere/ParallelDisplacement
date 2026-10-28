package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 点名收束 */
public class DamuhouNamedShot extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouNamedShot");

    public DamuhouNamedShot() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 5;
        baseMagicNumber = magicNumber = 2;
        printedCombo = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int hits = 1;
        if (targetHasAmp(m)) {
            hits += magicNumber;
        }
        comboHits(m, hits, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
