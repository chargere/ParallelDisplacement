package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.DamuhouVioletGlowPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/** 紫光扫射 */
public class DamuhouVioletGlow extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouVioletGlow");

    public DamuhouVioletGlow() {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new DamuhouVioletGlowPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeBaseCost(2);
    }
}
