package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.DamuhouCalmAimPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/** 冷静瞄准 */
public class DamuhouCalmAim extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouCalmAim");

    public DamuhouCalmAim() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 8;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new DamuhouCalmAimPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(6);
    }
}
