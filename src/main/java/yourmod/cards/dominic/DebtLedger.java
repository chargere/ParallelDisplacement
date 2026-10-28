package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.powers.DebtLedgerPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class DebtLedger extends AbstractDominicCard {
    public static final String ID = makeID("DebtLedger");

    public DebtLedger() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 1;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        AbstractPower existing = p.getPower(DebtLedgerPower.POWER_ID);
        if (existing instanceof DebtLedgerPower) {
            existing.stackPower(magicNumber);
            ((DebtLedgerPower) existing).stackMaxTriggers(secondMagic);
            existing.flash();
        } else {
            applyToSelf(new DebtLedgerPower(p, magicNumber, secondMagic));
        }
    }

    @Override
    public void upp() {
        upgradeSecondMagic(1);
    }
}
