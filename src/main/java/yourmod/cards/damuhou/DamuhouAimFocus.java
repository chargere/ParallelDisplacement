package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

/** 瞄准聚焦 */
public class DamuhouAimFocus extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouAimFocus");

    public DamuhouAimFocus() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 1;
        baseSecondMagic = secondMagic = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        DamuhouCombatState.focusTarget = m;
        DamuhouCombatState.focusBaseCombo = 1;
        if (upgraded) {
            addToBot(new DrawCardAction(p, magicNumber));
        }
    }

    @Override
    public void upp() {
    }
}
