package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

/** S1 快速退膛 */
public class DamuhouFastEject extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouFastEject");

    public DamuhouFastEject() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(p, magicNumber));
        DamuhouCombatState.addThisTurnNextCombo(1);
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
