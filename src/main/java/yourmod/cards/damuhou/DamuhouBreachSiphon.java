package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.BreachPower;

import static yourmod.ModFile.makeID;

/** 破绽回收 */
public class DamuhouBreachSiphon extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouBreachSiphon");

    public DamuhouBreachSiphon() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null && m.hasPower(BreachPower.POWER_ID)) {
            int stacks = m.getPower(BreachPower.POWER_ID).amount;
            addToBot(new RemoveSpecificPowerAction(m, p, BreachPower.POWER_ID));
            int energy = stacks / Math.max(1, magicNumber);
            if (energy > 0) {
                addToBot(new GainEnergyAction(energy));
            }
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(-1);
    }
}
