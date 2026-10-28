package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class FrostPuppet extends AbstractDominicCard {
    public static final String ID = makeID("FrostPuppet");

    public FrostPuppet() {
        super(ID, 2, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 19;
        baseMagicNumber = magicNumber = 3;
        setFrostPay(2, FrostPayType.RETURN_TO_VOID, 3);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        blck();
        atb(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber), magicNumber));
        atb(new ApplyPowerAction(p, p, new LoseStrengthPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upp() {
        upgradeBlock(4);
        upgradeMagicNumber(1);
    }
}
