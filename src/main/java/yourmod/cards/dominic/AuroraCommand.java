package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.AuroraCommandPower;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;
import static yourmod.util.Wiz.atb;

public class AuroraCommand extends AbstractDominicCard {
    public static final String ID = makeID("AuroraCommand");

    public AuroraCommand() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        baseSecondMagic = secondMagic = 2;
        setFrostPay(3, FrostPayType.RETURN_TO_VOID, 2);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        atb(new DrawCardAction(p, magicNumber));
        DominicCombatState.auroraCommandDiscountsLeft = Math.max(
                DominicCombatState.auroraCommandDiscountsLeft, secondMagic);
        applyToSelf(new AuroraCommandPower(p, secondMagic));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
