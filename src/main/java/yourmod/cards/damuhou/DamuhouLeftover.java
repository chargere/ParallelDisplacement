package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.DamuhouLeftoverPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/** 余弹装填 */
public class DamuhouLeftover extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouLeftover");

    public DamuhouLeftover() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 5;
        baseSecondMagic = secondMagic = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new DamuhouLeftoverPower(p, magicNumber, secondMagic));
    }

    @Override
    public void upp() {
        upgradeSecondMagic(1);
    }
}
