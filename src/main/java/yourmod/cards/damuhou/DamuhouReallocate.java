package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

/** 战术再分配 */
public class DamuhouReallocate extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouReallocate");

    public DamuhouReallocate() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(2));
        if (!upgraded) {
            DamuhouCombatState.nextAttackComboPenalty += magicNumber;
        }
    }

    @Override
    public void upp() {
    }
}
