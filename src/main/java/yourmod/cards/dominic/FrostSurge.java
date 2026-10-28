package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FrostColdPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class FrostSurge extends AbstractDominicCard {
    public static final String ID = makeID("FrostSurge");

    public FrostSurge() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 2;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        blck();
        if (FrostColdPower.getFrost(p) >= 4) {
            atb(new GainEnergyAction(2));
        }
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
        upgradeBlock(3);
    }
}
