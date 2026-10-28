package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FrostColdPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class IceWall extends AbstractDominicCard {
    public static final String ID = makeID("IceWall");

    public IceWall() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 6;
        baseMagicNumber = magicNumber = 6;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        blck();
        if (FrostColdPower.getFrost(p) >= 2) {
            atb(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, magicNumber));
        }
    }

    @Override
    public void upp() {
        upgradeBlock(3);
    }
}
