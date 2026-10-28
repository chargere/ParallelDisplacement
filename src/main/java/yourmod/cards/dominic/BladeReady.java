package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.BladeReadyPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class BladeReady extends AbstractDominicCard {
    public static final String ID = makeID("BladeReady");

    public BladeReady() {
        super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 8;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new BladeReadyPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeBaseCost(1);
    }
}
