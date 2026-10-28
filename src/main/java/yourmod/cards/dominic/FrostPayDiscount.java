package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FrostPayDiscountPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class FrostPayDiscount extends AbstractDominicCard {
    public static final String ID = makeID("FrostPayDiscount");

    public FrostPayDiscount() {
        super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new FrostPayDiscountPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeBaseCost(1);
    }
}
