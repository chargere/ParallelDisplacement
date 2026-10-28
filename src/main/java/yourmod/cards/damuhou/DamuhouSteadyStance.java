package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

/** S4 稳固姿态 */
public class DamuhouSteadyStance extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouSteadyStance");

    public DamuhouSteadyStance() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 6;
        baseMagicNumber = magicNumber = 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int b = block;
        if (DamuhouCombatState.attacksPlayedThisTurn <= 0) {
            b += magicNumber;
        }
        addToBot(new GainBlockAction(p, p, b));
    }

    @Override
    public void upp() {
        upgradeBlock(2);
        upgradeMagicNumber(2);
    }
}
