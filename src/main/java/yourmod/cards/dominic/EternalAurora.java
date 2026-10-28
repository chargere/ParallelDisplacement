package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.EternalAuroraPower;
import yourmod.powers.FrostColdPower;
import yourmod.powers.ReturnToVoidPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;
import static yourmod.util.Wiz.atb;

public class EternalAurora extends AbstractDominicCard {
    public static final String ID = makeID("EternalAurora");

    public EternalAurora() {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        if (FrostColdPower.getFrost(p) < 6) {
            atb(new ApplyPowerAction(p, p, new ReturnToVoidPower(p, magicNumber), magicNumber));
        }
        applyToSelf(new EternalAuroraPower(p));
    }

    @Override
    public void upp() {
        upgradeBaseCost(2);
    }
}
