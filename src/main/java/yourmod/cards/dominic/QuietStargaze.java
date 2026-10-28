package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.QuietStargazePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class QuietStargaze extends AbstractDominicCard {
    public static final String ID = makeID("QuietStargaze");

    public QuietStargaze() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new QuietStargazePower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
