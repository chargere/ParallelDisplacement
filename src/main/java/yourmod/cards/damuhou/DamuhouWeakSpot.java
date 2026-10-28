package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.DamuhouWeakSpotPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/** 弱点洞悉 */
public class DamuhouWeakSpot extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouWeakSpot");

    public DamuhouWeakSpot() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new DamuhouWeakSpotPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
