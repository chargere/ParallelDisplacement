package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FrostCapRiftPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class FrostCapRift extends AbstractDominicCard {
    public static final String ID = makeID("FrostCapRift");

    public FrostCapRift() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 8;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new FrostCapRiftPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeBaseCost(1);
    }
}
