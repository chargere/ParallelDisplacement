package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.DamuhouAmmoRationPower;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

/** 弹药配给 */
public class DamuhouAmmoRation extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouAmmoRation");

    public DamuhouAmmoRation() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(magicNumber));
        DamuhouCombatState.ammoRationActive = true;
        addToBot(new ApplyPowerAction(p, p, new DamuhouAmmoRationPower(p, 1), 1));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(-1);
    }
}
