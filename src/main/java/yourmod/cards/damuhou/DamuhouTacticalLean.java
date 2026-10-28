package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

/** S2 战术侧身 */
public class DamuhouTacticalLean extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouTacticalLean");

    public DamuhouTacticalLean() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 6;
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int b = block;
        if (DamuhouCombatState.attacksPlayedThisTurn > 0) {
            b += magicNumber;
        }
        addToBot(new GainBlockAction(p, p, b));
    }

    @Override
    public void upp() {
        upgradeBlock(2);
        upgradeMagicNumber(1);
    }
}
