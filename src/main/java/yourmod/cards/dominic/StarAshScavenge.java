package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FrostColdPower;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class StarAshScavenge extends AbstractDominicCard {
    public static final String ID = makeID("StarAshScavenge");

    public StarAshScavenge() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        selfRetain = true;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        FrostColdPower.gainFrost(p, magicNumber);
        if (DominicCombatState.frostPayCancelledThisTurn > 0) {
            atb(new DrawCardAction(p, 1));
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
