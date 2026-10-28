package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class AuroraVeil extends AbstractDominicCard {
    public static final String ID = makeID("AuroraVeil");

    public AuroraVeil() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 5;
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        int extra = DominicCombatState.frostGainedThisTurn * magicNumber;
        atb(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, block + extra));
    }

    @Override
    public void applyPowers() {
        int extra = DominicCombatState.frostGainedThisTurn * magicNumber;
        int realBase = baseBlock;
        baseBlock = realBase + extra;
        super.applyPowers();
        baseBlock = realBase;
        isBlockModified = block != baseBlock;
    }

    @Override
    public void upp() {
        upgradeBlock(2);
        upgradeMagicNumber(1);
    }
}
