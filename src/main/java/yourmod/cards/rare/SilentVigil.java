package yourmod.cards.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.SilentVigilPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/** 沉默的守望：未造成伤害则叠静默；攻击打出时结算。 */
public class SilentVigil extends AbstractEasyCard {
    public final static String ID = makeID("SilentVigil");

    public SilentVigil() {
        super(ID, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 9;
        baseSecondMagic = secondMagic = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new SilentVigilPower(p, magicNumber, secondMagic));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(6);
        upgradeSecondMagic(1);
    }
}
