package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class MakeWay extends AbstractDominicCard {
    public static final String ID = makeID("MakeWay");

    public MakeWay() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        exhaust = true;
        setFrostPay(2, FrostPayType.DELAY, 1);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        atb(new GainEnergyAction(1));
        atb(new DrawCardAction(p, 1));
        if (DominicCombatState.lastFrostPayCancelled) {
            atb(new DrawCardAction(p, 1));
        }
    }

    @Override
    public void upp() {
        exhaust = false;
    }
}
