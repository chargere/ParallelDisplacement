package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FrostColdPower;

import static yourmod.ModFile.makeID;

public class HalfFrostArmor extends AbstractDominicCard {
    public static final String ID = makeID("HalfFrostArmor");

    public HalfFrostArmor() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        baseBlock = 0;
        setFrostPay(2, FrostPayType.FREEZE_CRACK, 2);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        blck();
    }

    @Override
    public void applyPowers() {
        int frost = 0;
        if (AbstractDungeon.player != null) {
            frost = FrostColdPower.getFrost(AbstractDungeon.player);
        }
        baseBlock = frost * magicNumber;
        super.applyPowers();
        isBlockModified = block != baseBlock;
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
