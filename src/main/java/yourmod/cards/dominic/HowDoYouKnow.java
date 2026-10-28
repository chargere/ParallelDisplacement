package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.HowDoYouKnowPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class HowDoYouKnow extends AbstractDominicCard {
    public static final String ID = makeID("HowDoYouKnow");

    public HowDoYouKnow() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 5;
        baseBlock = 8;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new HowDoYouKnowPower(p, magicNumber, block));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        upgradeBlock(4);
    }
}
