package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.NextTurnFrostPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class ColdCore extends AbstractDominicCard {
    public static final String ID = makeID("ColdCore");

    public ColdCore() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 12;
        baseMagicNumber = magicNumber = 2;
        selfRetain = true;
        setFrostPay(2, FrostPayType.FREEZE_CRACK, 2);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        blck();
        applyToSelf(new NextTurnFrostPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeBlock(3);
        upgradeMagicNumber(1);
    }
}
