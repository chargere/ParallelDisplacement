package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.SharedWarmthPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class SharedWarmth extends AbstractDominicCard {
    public static final String ID = makeID("SharedWarmth");

    public SharedWarmth() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 8;
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        blck();
        applyToSelf(new SharedWarmthPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeBlock(3);
    }
}
