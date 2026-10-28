package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 空仓扫射 */
public class DamuhouEmptyMagSweep extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouEmptyMagSweep");

    public DamuhouEmptyMagSweep() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 2;
        baseMagicNumber = magicNumber = 6;
        printedCombo = 6;
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
