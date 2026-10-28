package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.ThinFrostDisciplinePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class ThinFrostDiscipline extends AbstractDominicCard {
    public static final String ID = makeID("ThinFrostDiscipline");

    public ThinFrostDiscipline() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 4;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new ThinFrostDisciplinePower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
    }
}
