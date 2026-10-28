package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.StarMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class ClearAPath extends AbstractDominicCard {
    public static final String ID = makeID("ClearAPath");

    public ClearAPath() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseBlock = 10;
        baseMagicNumber = magicNumber = 2;
        setFrostPay(2, FrostPayType.RETURN_TO_VOID, 1);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        blck();
        atb(new ApplyPowerAction(m, p, new StarMarkPower(m, magicNumber), magicNumber));
    }

    @Override
    public void upp() {
        upgradeBlock(4);
    }
}
