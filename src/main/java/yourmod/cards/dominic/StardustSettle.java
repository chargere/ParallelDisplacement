package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.StardustSettlePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class StardustSettle extends AbstractDominicCard {
    public static final String ID = makeID("StardustSettle");

    public StardustSettle() {
        super(ID, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        isEthereal = true;
        baseMagicNumber = magicNumber = 1;
        baseSecondMagic = secondMagic = 2;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new StardustSettlePower(p, magicNumber, secondMagic));
    }

    @Override
    public void upp() {
        isEthereal = false;
    }
}
