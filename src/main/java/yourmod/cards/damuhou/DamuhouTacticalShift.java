package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.BreachPower;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;

/** 战术转移 */
public class DamuhouTacticalShift extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouTacticalShift");

    public DamuhouTacticalShift() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null && m.hasPower(BreachPower.POWER_ID)) {
            int stacks = m.getPower(BreachPower.POWER_ID).amount;
            ArrayList<AbstractMonster> nbs = neighborsAround(m);
            if (!nbs.isEmpty() && stacks > 0) {
                AbstractMonster dest = nbs.get(nbs.size() - 1);
                addToBot(new RemoveSpecificPowerAction(m, p, BreachPower.POWER_ID));
                addToBot(new ApplyPowerAction(dest, p, new BreachPower(dest, stacks), stacks));
            }
        }
        addToBot(new DrawCardAction(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
