package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FrostColdPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class Stargaze extends AbstractDominicCard {
    public static final String ID = makeID("Stargaze");

    public Stargaze() {
        super(ID, 0, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        setFrostPay(4, FrostPayType.RETURN_TO_VOID, 2);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        FrostColdPower.gainFrost(p, magicNumber);
        atb(new DrawCardAction(p, 2));
        atb(new GainEnergyAction(2));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
