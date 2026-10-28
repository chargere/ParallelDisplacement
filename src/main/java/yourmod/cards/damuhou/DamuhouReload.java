package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

/** S7 装填 */
public class DamuhouReload extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouReload");

    public DamuhouReload() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(p, 2));
        DamuhouCombatState.addThisTurnNextCombo(magicNumber);
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
