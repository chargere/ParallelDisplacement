package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.StarMarkSensePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class StarMarkSense extends AbstractDominicCard {
    public static final String ID = makeID("StarMarkSense");

    public StarMarkSense() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new StarMarkSensePower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
    }
}
