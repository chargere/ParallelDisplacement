package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class RefrozenVeil extends AbstractDominicCard {
    public static final String ID = makeID("RefrozenVeil");

    public RefrozenVeil() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 14;
        baseSecondMagic = secondMagic = 10;
        setFrostPay(2, FrostPayType.DELAY, 2);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        blck();
        if (DominicCombatState.lostDexThisTurn) {
            atb(new GainBlockAction(p, p, secondMagic));
        }
    }

    @Override
    public void upp() {
        upgradeBlock(3);
        upgradeSecondMagic(2);
    }
}
