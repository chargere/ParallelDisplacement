package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 连射 */
public class DamuhouBurstStream extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouBurstStream");

    public DamuhouBurstStream() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 2;
        baseMagicNumber = magicNumber = 4;
        printedCombo = 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        comboHits(m, magicNumber, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
        printedCombo = magicNumber;
    }
}
