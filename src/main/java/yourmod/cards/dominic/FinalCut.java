package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.FinalCutChainAction;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class FinalCut extends AbstractDominicCard {
    public static final String ID = makeID("FinalCut");

    public FinalCut() {
        super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseDamage = 36;
        baseBlock = 12;
        isMultiDamage = true;
        exhaust = true;
        setFrostPay(6, FrostPayType.STAR_EXTINGUISH);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        atb(new FinalCutChainAction(p, multiDamage, block));
    }

    @Override
    public void upp() {
        upgradeDamage(6);
        upgradeBlock(4);
    }
}
