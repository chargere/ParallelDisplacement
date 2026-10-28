package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.TravelerChronicleAction;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class TravelerChronicle extends AbstractDominicCard {
    public static final String ID = makeID("TravelerChronicle");

    public TravelerChronicle() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        exhaust = true;
        setFrostPay(3, FrostPayType.RETURN_TO_VOID, 1);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        atb(new TravelerChronicleAction(magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
