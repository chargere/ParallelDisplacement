package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FrostColdPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class MeltSnow extends AbstractDominicCard {
    public static final String ID = makeID("MeltSnow");

    public MeltSnow() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 5;
        exhaust = true;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        if (FrostColdPower.getFrost(p) >= 3) {
            FrostColdPower.spendFrostActive(p, 3);
            atb(new HealAction(p, p, magicNumber));
        } else {
            atb(new LoseHPAction(p, p, 3));
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
    }
}
