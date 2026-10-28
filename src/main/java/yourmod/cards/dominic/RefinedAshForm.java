package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.RefinedAshFormPower;
import yourmod.powers.RefinedAshPower;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class RefinedAshForm extends AbstractDominicCard {
    public static final String ID = makeID("RefinedAshForm");

    public RefinedAshForm() {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        baseSecondMagic = secondMagic = 36;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        DominicCombatState.refinedAshBloomDamage = secondMagic;
        applyToSelf(new RefinedAshFormPower(p, secondMagic));
        applyToSelf(new RefinedAshPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeBaseCost(2);
        upgradeSecondMagic(6);
    }
}
