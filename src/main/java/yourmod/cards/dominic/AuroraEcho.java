package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class AuroraEcho extends AbstractDominicCard {
    public static final String ID = makeID("AuroraEcho");

    public AuroraEcho() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        int draw = magicNumber + DominicCombatState.frostPayCancelledThisTurn;
        if (draw > 0) {
            atb(new DrawCardAction(p, draw));
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
