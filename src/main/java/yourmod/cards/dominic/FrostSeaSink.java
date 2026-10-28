package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FrostColdPower;

import static yourmod.ModFile.makeID;

public class FrostSeaSink extends AbstractDominicCard {
    public static final String ID = makeID("FrostSeaSink");
    private static final int BASE_BLOCK = 10;

    public FrostSeaSink() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseBlock = BASE_BLOCK;
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        int frost = FrostColdPower.getFrost(p);
        applyPowers();
        blck();
        int spend = frost / 2;
        if (spend > 0) {
            FrostColdPower.spendFrostActive(p, spend);
        }
    }

    @Override
    public void applyPowers() {
        int frost = 0;
        if (AbstractDungeon.player != null) {
            frost = FrostColdPower.getFrost(AbstractDungeon.player);
        }
        baseBlock = BASE_BLOCK + frost * magicNumber;
        super.applyPowers();
        isBlockModified = block != baseBlock;
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
